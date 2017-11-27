package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static tzeth.preconds.MorePreconditions.checkNotNegative;

import java.util.Objects;

import javax.annotation.Nullable;

import jetoze.jambon.PlayingTime;

public final class GoalieStats {
    // TODO: Add seconds played
    private final int gamesPlayed;
    private final PlayingTime playingTime;
    private final int shotsAgainst;
    private final int goalsAgainst;

    public GoalieStats() {
        this(0, PlayingTime.none(), 0, 0);
    }

    // TODO: Too many int-arguments make this constructor very user-unfriendly
    // and error prone.
    public GoalieStats(int gamesPlayed, PlayingTime playingTime, int shotsAgainst,
            int goalsAgainst) {
        this.gamesPlayed = checkNotNegative(gamesPlayed);
        this.playingTime = checkNotNull(playingTime);
        this.shotsAgainst = checkNotNegative(shotsAgainst);
        this.goalsAgainst = checkNotNegative(goalsAgainst);
        checkArgument(shotsAgainst >= goalsAgainst);
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public PlayingTime getPlayingTime() {
        return playingTime;
    }

    public int getShotsAgainst() {
        return shotsAgainst;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getSaves() {
        return shotsAgainst - goalsAgainst;
    }

    public GoalieStats add(GoalieStats o) {
        return new GoalieStats(
                this.gamesPlayed + o.gamesPlayed,
                this.playingTime.add(o.playingTime), 
                this.shotsAgainst + o.shotsAgainst,
                this.goalsAgainst + o.goalsAgainst);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof GoalieStats) {
            GoalieStats that = (GoalieStats) obj;
            return (this.gamesPlayed == that.gamesPlayed)
                    && this.playingTime.equals(that.playingTime)
                    && (this.shotsAgainst == that.shotsAgainst)
                    && (this.goalsAgainst == that.goalsAgainst);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.gamesPlayed, this.playingTime, this.shotsAgainst,
                this.goalsAgainst);
    }

    @Override
    public String toString() {
        // TODO: Include time played (hh:mm:ss)
        return String.format("%d GP [%s], %d shots against, %d goals against", this.gamesPlayed,
                this.playingTime, this.shotsAgainst, this.goalsAgainst);
    }

}
