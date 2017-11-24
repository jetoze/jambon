package jetoze.jambon.db;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.function.Supplier;

import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerName;
import tzeth.exhume.XmlBuilder;
import tzeth.exhume.XmlBuilder.Element;
import tzeth.exhume.sax.ElementEnd;
import tzeth.exhume.sax.ElementStart;
import tzeth.exhume.sax.RootPath;
import tzeth.exhume.sax.StartOfElement;

final class PlayerMasterDetailsXml {
    public static XmlOutput build(PlayerMasterDetails details) {
        XmlBuilder xmlBuilder = buildXml(details);
        return new XmlOutput(xmlBuilder);
    }

    private static XmlBuilder buildXml(PlayerMasterDetails details) {
        XmlBuilder xmlBuilder = new XmlBuilder();
        Element player = xmlBuilder.root("Player").withAttribute("id", details.getId())
            .child("DateOfBirth").withValue(details.getBirthDate().toString()).close()
            .child("FirstName").withValue(details.getName().getFirstName()).close()
            .child("LastName").withValue(details.getName().getLastName()).close();
        Element seasons = player.child("Seasons");
        details.getActiveSeasons().forEach(s -> {
            seasons.child("Season").withValue(s.toString()).close();
        });
        return xmlBuilder;
    }
    
    public static PlayerMasterDetails fromXml(String xml) throws SAXException {
        return XmlUtils.loadFromXml(xml, new MasterDetailsBuilder());
    }
    
    public static PlayerMasterDetails fromFile(File file) throws SAXException, IOException {
        return XmlUtils.loadFromFile(file, new MasterDetailsBuilder());
    }
    
    
    // TODO: Once ExhumeSaxParser supports private handler, make this class private.
    @RootPath("/Player/")
    private static final class MasterDetailsBuilder implements Supplier<PlayerMasterDetails> {
        private String id;
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private ImmutableList.Builder<Season> seasons = ImmutableList.builder();
        
        @ElementStart("")
        public void player(StartOfElement soe) {
            this.id = soe.attributeValue("id");
        }
        
        @ElementEnd("FirstName")
        public void firstName(String value) {
            this.firstName = value;
        }
        
        @ElementEnd("LastName")
        public void lastName(String value) {
            this.lastName = value;
        }
        
        @ElementEnd("DateOfBirth")
        public void birthDate(LocalDate date) {
            this.birthDate = date;
        }
        
        @ElementEnd("Seasons/Season")
        public void season(String value) {
            seasons.add(Season.fromString(value));
        }
        
        @Override
        public PlayerMasterDetails get() {
            return new PlayerMasterDetails(this.id, new PlayerName(this.lastName, this.firstName),
                    this.birthDate, this.seasons.build());
        }
    }

    
    private PlayerMasterDetailsXml() {/**/}
}
