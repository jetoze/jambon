package jetoze.jambon;

import static tzeth.preconds.MorePreconditions.*;

import javax.annotation.Nullable;

public final class Season {
    private final int year;
    
    public Season(int year) {
        this.year = checkInRange(year, 1890, 2100);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Season) && (this.year == ((Season) obj).year);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.year);
    }

    @Override
    public String toString() {
        return Integer.toString(this.year);
    }
    
    /**
     * Creates a {@code Season} instance from the string returned by {@link #toString()}.
     */
    public static Season fromString(String s) {
        return new Season(Integer.parseInt(s));
    }
    
}
