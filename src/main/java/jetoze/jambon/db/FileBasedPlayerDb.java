package jetoze.jambon.db;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerStats;
import jetoze.jambon.player.Strengths;
import jetoze.jambon.util.Folder;
import tzeth.collections.ImCollectors;
import tzeth.concurrent.RW;
import tzeth.function.ThrowingFunction;

final class FileBasedPlayerDb extends PlayerDb {
    private static final FileFilter FILE_FILTER = f -> {
        String name = f.getName();
        return name.startsWith("player-") && name.endsWith(".xml");
    };

    private final Folder dir;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public FileBasedPlayerDb(Folder dir) {
        this.dir = checkNotNull(dir);
    }

    @Override
    public ImmutableSet<String> listAllPlayerIds() {
        // TODO: Or keep a separate master list of all IDs? This list could be cached in memory.
        return RW.read(rwLock).get(() -> dir.streamFiles(FILE_FILTER).map(this::idFromFile)
                .collect(ImCollectors.toSet()));
    }

    @Override
    public boolean contains(String playerId) {
        return RW.read(rwLock).test(playerId, this::containsImpl);
    }

    private Boolean containsImpl(String playerId) {
        File file = getFile(playerId);
        return file.canRead();
    }

    @Override
    public void storeMasterDetails(PlayerMasterDetails details) {
        RW.write(rwLock).consume(details, this::storeMasterDetailsImpl);
    }

    private void storeMasterDetailsImpl(PlayerMasterDetails details) {
        writeFile(() -> PlayerMasterDetailsXml.build(details),
                () -> getFile(details.getId()));
    }

    private void writeFile(Supplier<XmlOutput> xmlProducer, Supplier<File> fileProducer) {
        try {
            XmlOutput xml = xmlProducer.get();
            File file = fileProducer.get();
            xml.writeToFile(file);
        } catch (IOException e) {
            throw new DbException("Failed to write player data", e);
        }
    }
    
    @Override
    public PlayerMasterDetails loadMasterDetails(String playerId) {
        return RW.read(rwLock).apply(playerId, this::loadMasterDetailsImpl);
    }

    private PlayerMasterDetails loadMasterDetailsImpl(String playerId) {
        return readFile(() -> getFileForRead(playerId), PlayerMasterDetailsXml::fromFile);
    }
    
    private <T> T readFile(Supplier<File> fileProducer, ThrowingFunction<File, T> parser) {
        File file = fileProducer.get();
        try {
            T value = parser.apply(file);
            return value;
        } catch (Exception e) {
            throw new DbException("Failed to load player data", e);
        }
    }

    @Override
    public void storeStrengths(String playerId, Season season, Strengths strengths) {
        store(playerId, season, strengths, this::strengthsFile, PlayerStrengthsXml::build);
    }

    private <T> void store(String playerId, Season season, T value,
            BiFunction<String, Season, File> fileFactory, Function<T, XmlOutput> writer) {
        RW.write(rwLock).run(() -> storeImpl(playerId, season, value, fileFactory, writer));
    }

    private <T> void storeImpl(String playerId, Season season, T value,
            BiFunction<String, Season, File> fileFactory, Function<T, XmlOutput> writer) {
        writeFile(() -> writer.apply(value), () -> fileFactory.apply(playerId, season));
    }
    
    @Override
    public Strengths loadStrengths(String playerId, Season season) {
        return load(playerId, season, this::strengthsFile, PlayerStrengthsXml::fromFile);
    }
    
    private <T> T load(String playerId, Season season, 
            BiFunction<String, Season, File> fileFactory, ThrowingFunction<File, T> valueFactory) {
        return RW.read(rwLock).get(() -> loadImpl(playerId, season, fileFactory, valueFactory));
    }

    private <T> T loadImpl(String playerId, Season season, 
            BiFunction<String, Season, File> fileFactory, ThrowingFunction<File, T> valueFactory) {
        Supplier<File> fileSupplier = () -> {
            File file = fileFactory.apply(playerId, season);
            if (!file.canRead()) {
                throw new DbException("No such player and season: " + playerId + ", " + season);
            }
            return file;
        };
        return readFile(fileSupplier, valueFactory);
    }

    @Override
    public void storeStats(String playerId, Season season, PlayerStats stats) {
        store(playerId, season, stats, this::statsFile, PlayerStatsXml::build);
    }

    @Override
    public PlayerStats loadStats(String playerId, Season season) {
        return load(playerId, season, this::statsFile, PlayerStatsXml::fromFile);
    }

    private File getFile(String playerId) {
        String name = getFileName(playerId);
        return dir.getFile(name);
    }

    private File getFileForRead(String playerId) {
        File file = getFile(playerId);
        if (!file.canRead()) {
            throw new DbException("Unknown player: " + playerId);
        }
        return file;
    }

    private static String getFileName(String playerId) {
        return "player-" + playerId + ".xml";
    }

    private String idFromFile(File file) {
        String name = file.getName();
        return name.substring("player-".length(), name.length() - ".xml".length());
    }
    
    private Folder seasonFolder(Season season) {
        // TODO: Not guaranteed that season.toString() returns a valid folder name. It could,
        // for example, be something like "1920/21".
        Folder f = dir.subFolder(season.toString());
        f.createOnDisk();
        return f;
    }
    
    private Folder strengthsFolder(Season season) {
        Folder f = seasonFolder(season).subFolder("strengths");
        f.createOnDisk();
        return f;
    }
    
    private File strengthsFile(String playerId, Season season) {
        // TODO: Add "strengths" to the file name? The files are stored in a separate
        // folder, so not strictly necessary.
        String name = playerId + ".xml";
        return strengthsFolder(season).getFile(name);
    }
    
    private Folder statsFolder(Season season) {
        Folder f = seasonFolder(season).subFolder("stats");
        f.createOnDisk();
        return f;
    }
    
    private File statsFile(String playerId, Season season) {
        // TODO: Add "stats" to the file name? The files are stored in a separate
        // folder, so not strictly necessary.
        String name = playerId + ".xml";
        return statsFolder(season).getFile(name);
    }
}
