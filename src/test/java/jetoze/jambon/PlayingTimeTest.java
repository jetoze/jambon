package jetoze.jambon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public final class PlayingTimeTest {

    @Test
    public void testEqualsForEqualPlayingTimes() {
        int seconds = 100;
        PlayingTime p1 = new PlayingTime(seconds);
        assertEquals(p1, p1);
        PlayingTime p2 = new PlayingTime(seconds);
        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsForUnequalPlayingTimes() {
        PlayingTime p1 = new PlayingTime(100);
        PlayingTime p2 = new PlayingTime(200);
        assertFalse(p1.equals(p2));
    }
    
    @Test
    public void testHashCodeForEqualPlayingTimes() {
        int seconds = 100;
        PlayingTime p1 = new PlayingTime(seconds);
        PlayingTime p2 = new PlayingTime(seconds);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testAdd() {
        int s1 = 100;
        int s2 = 400;
        PlayingTime p1 = new PlayingTime(s1);
        PlayingTime p2 = p1.add(new PlayingTime(s2));
        assertEquals(s1 + s2, p2.getSeconds());
    }
}
