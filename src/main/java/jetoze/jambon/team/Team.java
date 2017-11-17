package jetoze.jambon.team;

import static jetoze.preconds.MorePreconditions.checkNotEmpty;

public final class Team {
	private final String id;
	private final String franchiseId;
	private final String name;

	public Team(String id, String franchiseId, String name) {
		this.id = checkNotEmpty(id);
		this.franchiseId = checkNotEmpty(franchiseId);
		this.name = checkNotEmpty(name);
	}

	public String getId() {
		return id;
	}

	public String getFranchiseId() {
		return franchiseId;
	}

	public String getName() {
		return name;
	}
	

}
