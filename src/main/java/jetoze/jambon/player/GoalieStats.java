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

	public GoalieStats() {
		this(0, 0, 0);
	}
	
	public GoalieStats(int gamesPlayed, int shotsAgainst, int goalsAgainst) {
		this.gamesPlayed = checkNotNegative(gamesPlayed);
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

	public GoalieStats addGameStats(GoalieGameStats gameStats) {
		return new GoalieStats(this.gamesPlayed + 1, this.shotsAgainst + gameStats.getShotsAgainst(),
				this.goalsAgainst + gameStats.getGoalsAgainst());
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GoalieStats) {
			GoalieStats that = (GoalieStats) obj;
			return (this.gamesPlayed == that.gamesPlayed) &&
					(this.shotsAgainst == that.shotsAgainst) &&
					(this.goalsAgainst == that.goalsAgainst);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.gamesPlayed, this.shotsAgainst, this.goalsAgainst);
	}

	@Override
	public String toString() {
		return String.format("%d GP, %d shots against, %d goals against", 
				this.gamesPlayed, this.shotsAgainst, this.goalsAgainst);
	}
	
}
