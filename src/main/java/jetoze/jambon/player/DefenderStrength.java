package jetoze.jambon.player;

import javax.annotation.Nullable;

public final class DefenderStrength implements Comparable<DefenderStrength> {
    private final double factor;

    public DefenderStrength(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    public int compareTo(DefenderStrength o) {
        return Double.compare(this.factor, o.factor);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof DefenderStrength && ((DefenderStrength) obj).compareTo(this) == 0);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(factor);
    }

    @Override
    public String toString() {
        return Double.toString(factor);
    }

}
