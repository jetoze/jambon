package jetoze.jambon.player;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

public final class GoalieStrength implements Comparable<GoalieStrength> {
    private final double savePercentage;

    public GoalieStrength(double savePercentage) {
        checkArgument(savePercentage > 0 && savePercentage < 1);
        this.savePercentage = savePercentage;
    }

    public double getSavePercentage() {
        return savePercentage;
    }

    public int compareTo(GoalieStrength o) {
        return Double.compare(this.savePercentage, o.savePercentage);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof GoalieStrength && ((GoalieStrength) obj).compareTo(this) == 0);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(savePercentage);
    }

    @Override
    public String toString() {
        return Double.toString(savePercentage);
    }
}
