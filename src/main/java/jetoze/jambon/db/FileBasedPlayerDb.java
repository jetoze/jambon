package jetoze.jambon.db;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDate;

import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerName;
import jetoze.jambon.util.Folder;
import tzeth.collections.ImCollectors;
import tzeth.exhume.XmlBuilder;
import tzeth.exhume.XmlBuilder.Element;
import tzeth.exhume.sax.ElementStart;
import tzeth.exhume.sax.ExhumeSaxParser;
import tzeth.exhume.sax.RootPath;
import tzeth.exhume.sax.StartOfElement;

final class FileBasedPlayerDb extends PlayerDb {
    // TODO: Refactor out the building and parsing of XML to a separate class than 
    // can be tested independently of the file system.
    
    private static final FileFilter FILE_FILTER = f -> {
        String name = f.getName();
        return name.startsWith("player-") && name.endsWith(".xml");
    };

    private final Folder dir;

    public FileBasedPlayerDb(Folder dir) {
        this.dir = checkNotNull(dir);
    }

    @Override
    public void storeMasterDetails(PlayerMasterDetails details) {
        File file = getFile(details.getId());
        XmlBuilder xmlBuilder = new XmlBuilder();
        Element player = xmlBuilder.root("Player").withAttribute("id", details.getId())
            .child("DateOfBirth").withValue(dateToString(details.getBirthDate())).close()
            .child("FirstName").withValue(details.getName().getFirstName()).close()
            .child("LastName").withValue(details.getName().getLastName()).close();
        Element seasons = player.child("Seasons");
        details.getActiveSeasons().forEach(s -> {
            seasons.child("Season").withValue(s.toString()).close();
        });
        try {
            xmlBuilder.writeToFile(file);
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
            MasterDetailsBuilder builder = new MasterDetailsBuilder();
            ExhumeSaxParser parser = new ExhumeSaxParser(builder);
            parser.parseFile(file);
            return builder.build();
        } catch (SAXException | IOException e) {
            throw new DbException("Failed to load player master details", e);
        }
    }

    private static String dateToString(LocalDate date) {
        return date.toString();
    }

    @Override
    public ImmutableSet<String> listAllPlayerIds() {
        return dir.streamFiles(FILE_FILTER).map(this::idFromFile).collect(ImCollectors.toSet());
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
    
    
    // TODO: Once ExhumeSaxParser supports private handler, make this class private.
    @RootPath("/Player/")
    public static final class MasterDetailsBuilder {
        private String id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private ImmutableList.Builder<Season> seasons = ImmutableList.builder();
        
        @ElementStart("")
        public void player(StartOfElement soe) {
            this.id = soe.attributeValue("id");
        }
        
        @ElementStart("FirstName")
        public void firstName(String value) {
            this.firstName = value;
        }
        
        @ElementStart("LastName")
        public void lastName(String value) {
            this.lastName = value;
        }
        
        @ElementStart("DateOfBirth")
        public void birthDate(LocalDate date) {
            this.birthDate = date;
        }
        
        @ElementStart("Seasons/Season")
        public void season(String value) {
            seasons.add(Season.fromString(value));
        }
        
        public PlayerMasterDetails build() {
            return new PlayerMasterDetails(this.id, new PlayerName(this.firstName, this.lastName),
                    this.birthDate, this.seasons.build());
        }
    }
}
