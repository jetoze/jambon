package jetoze.jambon.player;

import java.util.Objects;

import javax.annotation.Nullable;

public final class ForwardStrength implements Comparable<ForwardStrength> {
	private final double scoringFactor;
	private final double passingFactor;

	public ForwardStrength(double scoringFactor, double passingFactor) {
		this.scoringFactor = scoringFactor;
		this.passingFactor = passingFactor;
	}

	public double getScoringFactor() {
		return scoringFactor;
	}

	public double getPassingFactor() {
		return passingFactor;
	}

	public int compareTo(ForwardStrength o) {
		// TODO: This is not obvoiusly correct
		return Double.compare(this.scoringFactor + this.passingFactor, o.scoringFactor + o.passingFactor);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ForwardStrength) {
			ForwardStrength that = (ForwardStrength) obj;
			return (this.scoringFactor == that.scoringFactor) && (this.passingFactor == that.passingFactor);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(scoringFactor, passingFactor);
	}

	@Override
	public String toString() {
		return String.format("Scoring: %d. Passing: %d", scoringFactor, passingFactor);
	}

}
