package jetoze.jambon;

import static org.junit.Assert.*;
import org.junit.Test;

public final class SeasonTest {

    @Test
    public void testEqualsForEqualSeasons() {
        Season s1 = new Season(1950);
        assertEquals(s1, s1);
        Season s2 = new Season(1950);
        assertEquals(s1, s2);
    }

    @Test
    public void testHashCodeForEqualSeasons() {
        Season s1 = new Season(1950);
        Season s2 = new Season(1950);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
    
    @Test
    public void testEqualsForDifferentSeasons() {
        Season s1 = new Season(1950);
        assertEquals(s1, s1);
        Season s2 = new Season(1980);
        assertFalse(s1.equals(s2));
    }
    
    @Test
    public void testFromString() {
        Season s = new Season(1984);
        assertEquals(s, Season.fromString(s.toString()));
    }
}
