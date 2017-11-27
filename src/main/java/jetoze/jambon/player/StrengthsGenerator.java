package jetoze.jambon.player;

import java.util.Random;

import tzeth.exceptions.NotImplementedYetException;

public final class StrengthsGenerator {
    public static Strengths generateStrengths(Position p) {
        switch (p) {
        case GOALIE:
            throw new NotImplementedYetException();
        case DEFENDER:
            /* TODO: falling through to FORWARD for now */
        case FORWARD:
            ForwardStrength forward = new ForwardStrength(generateFactor(), generateFactor());
            DefenderStrength defender = new DefenderStrength(generateFactor());
            GoalieStrength goalie = new GoalieStrength(0.3);
            return new Strengths(forward, defender, goalie);
        default:
            throw new AssertionError("Unknown position: " + p);
        }
    }
    
    private static double generateFactor() {
        double start = 1.00;
        double max = 20.00;
        double min = 0.10;
        int iterations = 30;
        int delta = 0;
        Random rnd = new Random();
        for (int n = 0; n < iterations; ++n) {
            if (rnd.nextBoolean()) {
                ++delta;
            } else {
                --delta;
            }
        }
        if (delta > 0) {
            return start + delta * ((max - start) / iterations);
        } else if (delta < 0) {
            return start + delta * ((start - min) / iterations);
        } else {
            return start;
        }
    }
    
    private StrengthsGenerator() {/**/}

}
