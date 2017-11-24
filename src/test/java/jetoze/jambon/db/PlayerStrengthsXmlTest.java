package jetoze.jambon.db;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xml.sax.SAXException;

import jetoze.jambon.player.DefenderStrength;
import jetoze.jambon.player.ForwardStrength;
import jetoze.jambon.player.GoalieStrength;
import jetoze.jambon.player.Strengths;

public final class PlayerStrengthsXmlTest {

    @Test
    public void testStoreAndLoad() {
        Strengths s0 = new Strengths(
                new ForwardStrength(2.25, 1.72),
                new DefenderStrength(0.88),
                new GoalieStrength(0.12)
        );
        String xml = PlayerStrengthsXml.build(s0).toXml();
        try {
            Strengths s1 = PlayerStrengthsXml.fromXml(xml);
            assertEquals(s0, s1);
        } catch (SAXException e) {
            fail(e.getMessage());
        }
    }
}
