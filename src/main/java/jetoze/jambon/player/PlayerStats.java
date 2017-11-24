package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nullable;

public final class PlayerStats {
    private final ScoringStats scoring;
    private final GoalieStats goalie;

    public PlayerStats(ScoringStats scoring, GoalieStats goalie) {
        this.scoring = checkNotNull(scoring);
        this.goalie = checkNotNull(goalie);
    }

    public ScoringStats getScoringStats() {
        return scoring;
    }

    public GoalieStats getGoalieStats() {
        return goalie;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PlayerStats) {
            PlayerStats that = (PlayerStats) obj;
            return this.scoring.equals(that.scoring) &&
                    this.goalie.equals(that.goalie);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.scoring, this.goalie);
    }

    @Override
    public String toString() {
        return String.format("[%s] [%s]", this.scoring, this.goalie);
    }
    
}
