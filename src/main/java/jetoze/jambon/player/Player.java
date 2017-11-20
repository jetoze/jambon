package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkNotNull;
import static tzeth.preconds.MorePreconditions.checkNotEmpty;

import java.time.LocalDate;

import javax.annotation.Nullable;

public final class Player {
	private final String id;
	private final PlayerName name;
	private final LocalDate birthDate;
	private final Strengths strengths;
	
	public Player(String id, PlayerName name, LocalDate birthDate, Strengths strengths) {
		this.id = checkNotEmpty(id);
		this.name = checkNotNull(name);
		this.birthDate = checkNotNull(birthDate);
		this.strengths = checkNotNull(strengths);
	}

	public String getId() {
		return id;
	}

	public PlayerName getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Strengths getStrengths() {
		return strengths;
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", name.shortForm(), id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		return (obj instanceof Player) && ((Player) obj).id.equals(this.id);
	}
}
