package jetoze.jambon;

import static tzeth.preconds.MorePreconditions.checkNotNegative;

import javax.annotation.Nullable;

public final class PlayingTime {
    private final int seconds;

    public PlayingTime(int seconds) {
        this.seconds = checkNotNegative(seconds);
    }

    public static PlayingTime none() {
        return new PlayingTime(0);
    }

    public int getSeconds() {
        return seconds;
    }

    public PlayingTime add(PlayingTime o) {
        return new PlayingTime(this.seconds + o.seconds);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof PlayingTime) && (this.seconds == ((PlayingTime) obj).seconds);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.seconds);
    }

    @Override
    public String toString() {
        int minutes = seconds / 60;
        int rest = this.seconds - 60 * minutes;
        String secondsString = rest < 10 ? "0" + rest : Integer.toString(rest);
        return String.format("%d:%s", minutes, secondsString);
    }
}
