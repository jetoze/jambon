package jetoze.jambon.tools;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import jetoze.jambon.player.PlayerName;
import tzeth.collections.ImCollectors;

public final class PlayerListCreator {
    private static final String RETRO_SHEET_FILE = "/Users/torgil/coding/files/jambon/retro-sheet/mlb_player_names_and_ids.txt";
    
    public PlayerListCreator() {
        // TODO Auto-generated constructor stub
    }
    
    public static void main(String[] args) throws Exception {
        loadIdsAndNames().forEach(System.out::println);
    }

    private static ImmutableSet<IdAndName> loadIdsAndNames() throws Exception {
        return Files.lines(new File(RETRO_SHEET_FILE).toPath())
            .filter(s -> !s.startsWith("#"))
            .map(IdAndName::fromLine)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(ImCollectors.toSet());
    }
    
    
    private static class IdAndName {
        private final String id;
        private final PlayerName name;
        
        public IdAndName(String id, PlayerName name) {
            this.id = id;
            this.name = name;
        }
        
        public static Optional<IdAndName> fromLine(String s) {
            String[] parts = s.split(",");
            String id = parts[0];
            String lastName = unquote(parts[1]);
            String firstName = unquote(parts[2]);
            if (firstName.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new IdAndName(id, new PlayerName(lastName, firstName)));
        }
        
        private static String unquote(String s) {
            if (s.equals("")) {
                return "";
            }
            return (s.startsWith("") && s.endsWith(""))
                    ? s.substring(1, s.length() - 1)
                    : s;
        }
        
        @Override
        public String toString() {
            return String.format("%s [%s]", name.shortForm(), id);
        }
    }
    
}
