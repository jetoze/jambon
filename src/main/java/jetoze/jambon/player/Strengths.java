package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nullable;

public final class Strengths {
	private final ForwardStrength forwardStrength;
	private final DefenderStrength defenderStrength;
	private final GoalieStrength goalieStrength;

	public Strengths(ForwardStrength forwardStrength, DefenderStrength defenderStrength,
			GoalieStrength goalieStrength) {
		this.forwardStrength = checkNotNull(forwardStrength);
		this.defenderStrength = checkNotNull(defenderStrength);
		this.goalieStrength = checkNotNull(goalieStrength);
	}

	public ForwardStrength getForwardStrength() {
		return forwardStrength;
	}

	public DefenderStrength getDefenderStrength() {
		return defenderStrength;
	}

	public GoalieStrength getGoalieStrength() {
		return goalieStrength;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Strengths) {
			Strengths that = (Strengths) obj;
			return this.goalieStrength.equals(that.goalieStrength) &&
					this.defenderStrength.equals(that.defenderStrength) &&
					this.forwardStrength.equals(that.forwardStrength);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(goalieStrength, defenderStrength, forwardStrength);
	}

	@Override
	public String toString() {
		return String.format("Goalie: %s. Defender: %s. Forward: %s.", 
				goalieStrength, defenderStrength, forwardStrength);
	}

}
