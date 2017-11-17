package jetoze.jambon.player;

import static jetoze.preconds.MorePreconditions.checkNotNegative;

import java.util.Objects;

import javax.annotation.Nullable;

public final class PlayerScoringStats {
	private final int gamesPlayed;
	private final int goalsScored;
	private final int assists;

	public PlayerScoringStats() {
		this(0, 0, 0);
	}
	
	public PlayerScoringStats(int gamesPlayed, int goalsScored, int assists) {
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
	
	public PlayerScoringStats addGameStats(GameScoringStats gameStats) {
		return new PlayerScoringStats(this.gamesPlayed + 1, this.goalsScored + gameStats.getGoalsScored(), 
				this.assists + gameStats.getAssists());
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof PlayerScoringStats) {
			PlayerScoringStats that = (PlayerScoringStats) obj;
			return (this.gamesPlayed == that.gamesPlayed) &&
					(this.goalsScored == that.goalsScored) &&
					(this.assists == that.assists);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gamesPlayed, goalsScored, assists);
	}

	@Override
	public String toString() {
		return String.format("%d GP, %d goals, %d assists", this.gamesPlayed, this.goalsScored, this.assists);
	}
	

}
