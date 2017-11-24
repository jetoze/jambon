package jetoze.jambon.db;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableSet;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerStats;
import jetoze.jambon.player.Strengths;
import jetoze.jambon.util.Folder;
import tzeth.collections.ImCollectors;

final class FileBasedPlayerDb extends PlayerDb {
    private static final FileFilter FILE_FILTER = f -> {
        String name = f.getName();
        return name.startsWith("player-") && name.endsWith(".xml");
    };

    private final Folder dir;

    public FileBasedPlayerDb(Folder dir) {
        this.dir = checkNotNull(dir);
    }

    @Override
    public ImmutableSet<String> listAllPlayerIds() {
        // TODO: Or keep a separate master list of all IDs? This list could be cached in memory.
        return dir.streamFiles(FILE_FILTER).map(this::idFromFile).collect(ImCollectors.toSet());
    }

    @Override
    public void storeMasterDetails(PlayerMasterDetails details) {
        try {
            XmlOutput output = PlayerMasterDetailsXml.build(details);
            File file = getFile(details.getId());
            output.writeToFile(file);
        } catch (IOException e) {
            throw new DbException("Failed to write player master details", e);
        }
    }

    @Override
    public PlayerMasterDetails loadMasterDetails(String playerId) {
        File file = getFile(playerId);
        if (!file.canRead()) {
            throw new DbException("Unknown player: " + playerId);
        }
        try {
            return PlayerMasterDetailsXml.fromFile(file);
        } catch (SAXException | IOException e) {
            throw new DbException("Failed to load player master details", e);
        }
    }

    @Override
    public void storeStrengths(String playerId, Season season, Strengths strengths) {
        try {
            File file = strengthsFile(playerId, season);
            PlayerStrengthsXml.build(strengths).writeToFile(file);
        } catch (IOException e) {
            throw new DbException("Failed to write player strengths", e);
        }
    }

    @Override
    public Strengths loadStrengths(String playerId, Season season) {
        // TODO: Identical structure to loadMasterDetails (and the coming loadStats).
        // Refactor out to a common utility method?
        File file = strengthsFile(playerId, season);
        if (!file.canRead()) {
            throw new DbException("No such player and season: " + playerId + ", " + season);
        }
        try {
            return PlayerStrengthsXml.fromFile(file);
        } catch (SAXException | IOException e) {
            throw new DbException("Failed to load player strengths", e);
        }
    }

    @Override
    public void storeStats(String playerId, Season season, PlayerStats stats) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PlayerStats loadStats(String playerId, Season season) {
        // TODO Auto-generated method stub
        return null;
    }

    private File getFile(String playerId) {
        String name = getFileName(playerId);
        return dir.getFile(name);
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
}
