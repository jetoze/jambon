package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkArgument;
import static tzeth.preconds.MorePreconditions.checkNotNegative;

import java.util.Objects;

import javax.annotation.Nullable;

public final class GoalieStats {
    // TODO: Add seconds played
    private final int gamesPlayed;
    private final int shotsAgainst;
    private final int goalsAgainst;
    private final int secondsPlayed;

    public GoalieStats() {
        this(0, 0, 0, 0);
    }

    // TODO: Too many int-arguments make this constructor very user-unfriendly and error prone.
    public GoalieStats(int gamesPlayed, int secondsPlayed, int shotsAgainst, int goalsAgainst) {
        this.gamesPlayed = checkNotNegative(gamesPlayed);
        this.secondsPlayed = checkNotNegative(secondsPlayed);
        this.shotsAgainst = checkNotNegative(shotsAgainst);
        this.goalsAgainst = checkNotNegative(goalsAgainst);
        checkArgument(shotsAgainst >= goalsAgainst);
    }

    public int getGamesPlayed() {
        return gamesPlayed;
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

    public int getSecondsPlayed() {
        return secondsPlayed;
    }

    public GoalieStats add(GoalieStats o) {
        return new GoalieStats(this.gamesPlayed + o.gamesPlayed,
                this.secondsPlayed + o.secondsPlayed,
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
                    && (this.secondsPlayed == that.secondsPlayed)
                    && (this.shotsAgainst == that.shotsAgainst)
                    && (this.goalsAgainst == that.goalsAgainst);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.gamesPlayed, this.secondsPlayed, this.shotsAgainst, this.goalsAgainst);
    }

    @Override
    public String toString() {
        // TODO: Include time played (hh:mm:ss)
        return String.format("%d GP, %d shots against, %d goals against", this.gamesPlayed,
                this.shotsAgainst, this.goalsAgainst);
    }

}
