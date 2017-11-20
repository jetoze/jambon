package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkArgument;
import static tzeth.preconds.MorePreconditions.checkNotNegative;

public final class GoalieGameStats {
	// TODO: Add seconds played
	private final int shotsAgainst;
	private final int goalsAgainst;

	public GoalieGameStats(int shotsAgainst, int goalsAgainst) {
		this.shotsAgainst = checkNotNegative(shotsAgainst);
		this.goalsAgainst = checkNotNegative(goalsAgainst);
		checkArgument(shotsAgainst >= goalsAgainst);
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
	
	@Override
	public String toString() {
		return String.format("%d / %d", getSaves(), getShotsAgainst());
	}
	

}
