package jetoze.jambon.game;

import static com.google.common.base.Preconditions.checkNotNull;
import static jetoze.jambon.util.MorePreconditions.checkNotEmpty;

import java.time.LocalDate;

public final class GameData { // TODO: Come up with a better name
	private final String homeTeamId;
	private final String visitingTeamId;
	private final LocalDate date;

	public GameData(String homeTeamId, String visitingTeamId, LocalDate date) {
		this.homeTeamId = checkNotEmpty(homeTeamId);
		this.visitingTeamId = checkNotEmpty(visitingTeamId);
		this.date = checkNotNull(date);
	}

	public String getHomeTeamId() {
		return homeTeamId;
	}

	public String getVisitingTeamId() {
		return visitingTeamId;
	}

	public LocalDate getDate() {
		return date;
	}

}
