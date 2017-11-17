package jetoze.jambon.team;

import java.util.List;

import com.google.common.collect.ImmutableList;

import jetoze.jambon.player.Player;

public final class Roster {
	private final ImmutableList<Player> goalies;
	private final ImmutableList<Player> defenders;
	private final ImmutableList<Player> forwards;
	
	public Roster(List<Player> goalies, List<Player> defenders, List<Player> forwards) {
		this.goalies = ImmutableList.copyOf(goalies);
		this.defenders = ImmutableList.copyOf(defenders);
		this.forwards = ImmutableList.copyOf(forwards);
	}

	public ImmutableList<Player> getGoalies() {
		return goalies;
	}

	public ImmutableList<Player> getDefenders() {
		return defenders;
	}

	public ImmutableList<Player> getForwards() {
		return forwards;
	}
	

}
