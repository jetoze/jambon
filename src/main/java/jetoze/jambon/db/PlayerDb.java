package jetoze.jambon.db;

import com.google.common.collect.ImmutableSet;

import jetoze.jambon.Season;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.Strengths;
import jetoze.jambon.util.Folder;

/**
 * Defines a database of players.
 * <p>
 * TODO: More here
 */
public abstract class PlayerDb {

    /*
     * What is a "Player"? We have separate pieces of information:
     * 
     * + Invariants: The player's ID, name, and birthdate. These never changes,
     *   and should be stored separately.
     * + The seasons a player was active. This is not an invariant, but we have choices where to 
     *   store this info. We could store it separately (in a separate table, if you will), or
     *   we could store it together with the invariants. That storage place would then serve as a
     *   master table of player info.
     * + Strengths: This changes over the course of a player's career. When looking up the strengths
     *   of a player, we need to provide both the ID, and the season we are interested in.
     * + Team associations: A player can represent several teams during his career, or even a season. 
     *   Given a player ID and a season, we should be able to look up the team(s) he played for that
     *   year. We could keep it simple to begin with, and enforce a league rule that prevents a 
     *   player from switching teams during a season.
     * + Stats: Similar to strengths, in that each season will have its own set of stats, that need
     *   to be stored separately.
     * + Game log: Store info about each individual game a player played in during a season. 
     *   We have a choice here too, in that the game log could serve as the Stats database as well.
     *   Storing the accumulated stats separately is superfluous from a data point of view, but 
     *   could still make sense for performance reasons, as we otherwise will have to iterate over
     *   the game log everytime we want to get a piece of stat for a player.
     * + Injuries / Disabled List: If a player gets hurt during a season, this information needs to
     *   be stored somewhere too, so that we know where we left off when we reload a season that's
     *   in progress.
     *   
     * Next question is how all this should be represented outside of the DB. Does all this go into
     * the "Player" class? Or does the "Player" class represent a player in a given year?
     */

    public abstract ImmutableSet<String> listAllPlayerIds();
    
    public abstract void storeMasterDetails(PlayerMasterDetails details);
    
    public abstract PlayerMasterDetails loadMasterDetails(String playerId);

    public abstract void storeStrengths(String playerId, Season season, Strengths strengths);
    
    public abstract Strengths loadStrengths(String playerId, Season season);
    
    public static PlayerDb fileBased(Folder dir) {
        dir.createOnDisk();
        return new FileBasedPlayerDb(dir);
    }

}
