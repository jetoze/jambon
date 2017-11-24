package jetoze.jambon.db;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import org.xml.sax.SAXException;

import jetoze.jambon.player.GoalieStats;
import jetoze.jambon.player.PlayerStats;
import jetoze.jambon.player.ScoringStats;
import tzeth.exhume.XmlBuilder;
import tzeth.exhume.XmlBuilder.Element;
import tzeth.exhume.sax.ElementEnd;
import tzeth.exhume.sax.RootPath;

final class PlayerStatsXml {
    public static XmlOutput build(PlayerStats stats) {
        XmlBuilder xml = new XmlBuilder();
        ScoringStats scoringStats = stats.getScoringStats();
        GoalieStats goalieStats = stats.getGoalieStats();
        Element goalieElement = xml.root("Stats")
            .child("Scoring")
                .child("Gm").withValue(scoringStats.getGamesPlayed()).close()
                .child("G").withValue(scoringStats.getGoalsScored()).close()
                .child("A").withValue(scoringStats.getAssists()).close()
            .close()
            .child("Goalie");
        // Only write goalie stats if the player has actually played as a goalie.
        if (goalieStats.getGamesPlayed() > 0) {
            goalieElement.child("Gm").withValue(goalieStats.getGamesPlayed()).close()
                .child("Tm").withValue(goalieStats.getSecondsPlayed()).close()
                .child("SA").withValue(goalieStats.getShotsAgainst()).close()
                .child("GA").withValue(goalieStats.getGoalsAgainst());
        }
        return new XmlOutput(xml);
    }
    
    public static PlayerStats loadFromFile(File file) throws IOException, SAXException {
        return XmlUtils.loadFromFile(file, new Parser());
    }

    public static PlayerStats loadFromXml(String xml) throws SAXException {
        return XmlUtils.loadFromXml(xml, new Parser());
    }
    
    
    @RootPath("/Stats")
    private static class Parser implements Supplier<PlayerStats> {
        private int scoringStatsGames;
        private int goalsScored;
        private int assists;
        private int gamesAsGoale;
        private int secondsPlayedAsGoalie;
        private int shotsAgainst;
        private int goalsAgainst;
        
        @Override
        public PlayerStats get() {
            ScoringStats scoring = new ScoringStats(this.scoringStatsGames, this.goalsScored, this.assists);
            GoalieStats goalie = new GoalieStats(this.gamesAsGoale, this.secondsPlayedAsGoalie, this.shotsAgainst, this.goalsAgainst);
            return new PlayerStats(scoring, goalie);
        }
        
        @ElementEnd("Scoring/Gm")
        public void scoringStatsGames(Integer value) {
            this.scoringStatsGames = value;
        }
        
        @ElementEnd("Scoring/G")
        public void goalsScored(Integer value) {
            this.goalsScored = value;
        }
        
        @ElementEnd("Scoring/A")
        public void assists(Integer value) {
            this.assists = value;
        }
        
        @ElementEnd("Goalie/Gm")
        public void gamesAsGoalie(Integer value) {
            this.gamesAsGoale = value;
        }
        
        @ElementEnd("Goalie/Tm")
        public void secondsPlayedAsGoalie(Integer value) {
            this.secondsPlayedAsGoalie = value;
        }
        
        @ElementEnd("Goalie/SA")
        public void shotsAgainst(Integer value) {
            this.shotsAgainst = value;
        }
        
        @ElementEnd("Goalie/GA")
        public void goalisAgainst(Integer value) {
            this.goalsAgainst = value;
        }
    }

    
    private PlayerStatsXml() {/**/}
}
