package jetoze.jambon.player;

import static com.google.common.base.Preconditions.*;
import static tzeth.preconds.MorePreconditions.*;
import java.time.LocalDate;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import jetoze.jambon.Season;

public final class PlayerMasterDetails {
    private final String id;
    private final PlayerName name;
    private final LocalDate birthDate;
    private final ImmutableList<Season> activeSeasons;
    
    public PlayerMasterDetails(String id, PlayerName name, LocalDate birthDate,
            ImmutableList<Season> activeSeasons) {
        this.id = checkNotEmpty(id);
        this.name = checkNotNull(name);
        this.birthDate = checkNotNull(birthDate);
        this.activeSeasons = checkNotNull(activeSeasons);
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

    public ImmutableList<Season> getActiveSeasons() {
        return activeSeasons;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PlayerMasterDetails) {
            PlayerMasterDetails that = (PlayerMasterDetails) obj;
            return this.id.equals(that.id) &&
                    this.name.equals(that.name) &&
                    this.birthDate.equals(that.birthDate) &&
                    this.activeSeasons.equals(that.activeSeasons);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.birthDate, this.activeSeasons);
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", this.name.shortForm(), this.id);
    }
    
}
