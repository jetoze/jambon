package jetoze.jambon.db;

import static org.junit.Assert.*;
    
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Test;
import org.xml.sax.SAXException;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerName;

public final class PlayerMasterDetailsXmlTest {

    @Test
    public void testStoreAndLoad() {
        PlayerMasterDetails p0 = new PlayerMasterDetails("P-123", new PlayerName("John", "Doe"), 
                LocalDate.of(1950, 2, 2), Arrays.asList(new Season(1972), new Season(1973), new Season(1974)));
        String xml = PlayerMasterDetailsXml.build(p0).toXml();
        try {
            PlayerMasterDetails p1 = PlayerMasterDetailsXml.fromXml(xml);
            assertEquals(p0, p1);
        } catch (SAXException e) {
            fail(e.getMessage());
        }
    }
}
