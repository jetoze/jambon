package jetoze.jambon.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import jetoze.jambon.db.PlayerDb;
import jetoze.jambon.player.PlayerMasterDetails;
import jetoze.jambon.player.PlayerName;
import jetoze.jambon.util.Folder;
import tzeth.collections.ImCollectors;

public final class PlayerListCreator {
    private static final String RETRO_SHEET_FILE = "/Users/tzethson/fun/files/jambon/retrosheet/mlb_player_names_and_ids.txt";
    private static final Folder PLAYER_DB_DIR = new Folder("/Users/tzethson/fun/files/jambon/player_db");
    
    public static void main(String[] args) throws Exception {
        ImmutableSet<IdAndName> idsAndNames = loadIdsAndNames();
        PlayerDb db = PlayerDb.fileBased(PLAYER_DB_DIR);
        idsAndNames.forEach(ian -> {
            Info info = scrapePlayerInfo(ian);
            if (info != null) {
                PlayerMasterDetails pmd = new PlayerMasterDetails(ian.id, ian.name, info.birthDate, ImmutableList.of());
                db.storeMasterDetails(pmd);
            }
        });
    }

    private static ImmutableSet<IdAndName> loadIdsAndNames() throws Exception {
        return Files.lines(new File(RETRO_SHEET_FILE).toPath())
            .filter(s -> !s.startsWith("#"))
            .map(IdAndName::fromLine)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(ImCollectors.toSet());
    }
    
    @Nullable
    private static Info scrapePlayerInfo(IdAndName ian) {
        try {
            String url = String.format("http://www.retrosheet.org/boxesetc/%s/P%s.htm",
                    ian.name.getLastName().charAt(0), ian.id);
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet get = new HttpGet(url);
                try (CloseableHttpResponse response = httpClient.execute(get)) {
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        HttpEntity entity = response.getEntity();
                        String payload = EntityUtils.toString(entity);
                        return Info.fromHtml(payload);
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            String debutAsPlayer = parts[3];
            if (debutAsPlayer.isEmpty()) {
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
    
    
    private static class Info {
        private static final DateTimeFormatter BIRTH_DATE_PATTERN = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        private final LocalDate birthDate;
        private final ImmutableList<Integer> years;
        
        public Info(LocalDate birthDate, ImmutableList<Integer> years) {
            this.birthDate = birthDate;
            this.years = years;
        }

        @Nullable
        public static Info fromHtml(String html) {
            LocalDate birthDate = scrapeBirthDate(html);
            if (birthDate == null) {
                return null;
            }
            // TODO: Scrape years
            return new Info(birthDate, ImmutableList.of());
        }
        
        @Nullable
        private static LocalDate scrapeBirthDate(String html) {
            String birthDateMarker = "<TR><TD>Born ";
            int birthDateMarkerIndex = html.indexOf(birthDateMarker);
            if (birthDateMarkerIndex == -1) {
                return null;
            }
            int birthDateEndIndex = html.indexOf(", <A href", birthDateMarkerIndex);
            if (birthDateEndIndex == -1) {
                return null;
            }
            try {
                String birthDateRawString = html.substring(birthDateMarkerIndex + birthDateMarker.length(), 
                        birthDateEndIndex).replaceAll("\\s+", " ");
                return LocalDate.parse(birthDateRawString, BIRTH_DATE_PATTERN);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return null;
            }
        }
    }
    
}
