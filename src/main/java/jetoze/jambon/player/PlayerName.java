package jetoze.jambon.player;

import static tzeth.preconds.MorePreconditions.checkNotEmpty;

import java.util.Objects;

import javax.annotation.Nullable;

public final class PlayerName {
    private final String lastName;
    private final String firstName;

    public PlayerName(String lastName, String firstName) {
        this.lastName = checkNotEmpty(lastName);
        this.firstName = checkNotEmpty(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String fullForm() {
        return firstName + " " + lastName;
    }

    public String shortForm() {
        return String.format("%s, %s.", lastName, firstName.charAt(0));
    }

    @Override
    public String toString() {
        return fullForm();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PlayerName) {
            PlayerName that = (PlayerName) obj;
            return this.firstName.equals(that.firstName) && this.lastName.equals(that.lastName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

}
