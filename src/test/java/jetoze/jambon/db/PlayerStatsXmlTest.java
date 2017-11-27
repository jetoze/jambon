package jetoze.jambon.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.xml.sax.SAXException;

import jetoze.jambon.PlayingTime;
import jetoze.jambon.player.GoalieStats;
import jetoze.jambon.player.PlayerStats;
import jetoze.jambon.player.ScoringStats;

public final class PlayerStatsXmlTest {

    @Test
    public void testStoreAndLoad() {
        PlayerStats p0 = new PlayerStats(
                new ScoringStats(75, 1, 4),
                new GoalieStats(75, new PlayingTime(10_000), 1_500, 177));
        String xml = PlayerStatsXml.build(p0).toXml();
        try {
            PlayerStats p1 = PlayerStatsXml.fromXml(xml);
            assertEquals(p0, p1);
        } catch (SAXException e) {
            fail(e.getMessage());
        }
    }
}
