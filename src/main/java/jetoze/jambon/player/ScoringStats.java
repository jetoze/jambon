package jetoze.jambon.player;

import static com.google.common.base.Preconditions.*;
import static tzeth.preconds.MorePreconditions.checkNotNegative;

import java.util.Objects;

import javax.annotation.Nullable;

public final class ScoringStats {
    private final int gamesPlayed;
    private final int goalsScored;
    private final int assists;

    public ScoringStats() {
        this(0, 0, 0);
    }

    // TODO: Too many int-arguments make this constructor very user-unfriendly and error prone.
    public ScoringStats(int gamesPlayed, int goalsScored, int assists) {
        this.gamesPlayed = checkNotNegative(gamesPlayed);
        this.goalsScored = checkNotNegative(goalsScored);
        this.assists = checkNotNegative(assists);
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getAssists() {
        return assists;
    }

    public int getPoints() {
        return goalsScored + assists;
    }

    public ScoringStats add(ScoringStats o) {
        return new ScoringStats(this.gamesPlayed + o.gamesPlayed,
                this.goalsScored + o.goalsScored,
                this.assists + o.assists);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ScoringStats) {
            ScoringStats that = (ScoringStats) obj;
            return (this.gamesPlayed == that.gamesPlayed) && (this.goalsScored == that.goalsScored)
                    && (this.assists == that.assists);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gamesPlayed, goalsScored, assists);
    }

    @Override
    public String toString() {
        return String.format("%d GP, %d goals, %d assists", 
                this.gamesPlayed, this.goalsScored, this.assists);
    }

}
