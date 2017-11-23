package jetoze.jambon.db;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

import jetoze.jambon.player.DefenderStrength;
import jetoze.jambon.player.ForwardStrength;
import jetoze.jambon.player.GoalieStrength;
import jetoze.jambon.player.Strengths;
import tzeth.exhume.XmlBuilder;
import tzeth.exhume.sax.ElementEnd;
import tzeth.exhume.sax.ExhumeSaxParser;
import tzeth.exhume.sax.RootPath;

final class StrengthsXml {
    public static XmlOutput build(Strengths strengths) {
        XmlBuilder xml = new XmlBuilder();
        xml.root("Strengths")
            .child("Goalie")
                .child("Factor").withValue(strengths.getGoalieStrength().getSavePercentage()).close()
            .close()
            .child("Defender")
                .child("Factor").withValue(strengths.getDefenderStrength().getFactor()).close()
            .close()
            .child("Forward")
                .child("Score").withValue(strengths.getForwardStrength().getScoringFactor()).close()
                .child("Pass").withValue(strengths.getForwardStrength().getPassingFactor());
        return new XmlOutput(xml);
    }
    
    public static Strengths fromXml(String xml) throws SAXException {
        Builder builder = new Builder();
        ExhumeSaxParser parser = new ExhumeSaxParser(builder);
        parser.parseXml(xml);
        return builder.build();
    }
    
    public static Strengths fromFile(File file) throws SAXException, IOException {
        Builder builder = new Builder();
        ExhumeSaxParser parser = new ExhumeSaxParser(builder);
        parser.parseFile(file);
        return builder.build();
    }
    
    
    @RootPath("Strengths/")
    private static class Builder {
        private double goalie;
        private double defensive;
        private double scoring;
        private double passing;
        
        @ElementEnd("Goalie/Factor")
        public void goalie(Double value) {
            this.goalie = value;
        }
        
        @ElementEnd("Defender/Factor")
        public void defensive(Double value) {
            this.defensive = value;
        }
        
        @ElementEnd("Forward/Score")
        public void scoring(Double value) {
            this.scoring = value;
        }
        
        @ElementEnd("Forward/Pass")
        public void passing(Double value) {
            this.passing = value;
        }
        
        public Strengths build() {
            return new Strengths(
                    new ForwardStrength(this.scoring, this.passing), 
                    new DefenderStrength(this.defensive), 
                    new GoalieStrength(this.goalie));
        }
    }
}
