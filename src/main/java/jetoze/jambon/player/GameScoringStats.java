package jetoze.jambon.player;

import static jetoze.jambon.util.MorePreconditions.checkNotNegative;

public final class GameScoringStats {
	private final int goalsScored;
	private final int assists;

	public GameScoringStats(int goalsScored, int assists) {
		this.goalsScored = checkNotNegative(goalsScored);
		this.assists = checkNotNegative(assists);
	}

	public int getGoalsScored() {
		return this.goalsScored;
	}

	public int getAssists() {
		return this.assists;
	}
	
	public int getPoints() {
		return this.goalsScored + this.assists;
	}

	@Override
	public String toString() {
		return String.format("%d G %d A", this.goalsScored, this.assists);
	}

}
