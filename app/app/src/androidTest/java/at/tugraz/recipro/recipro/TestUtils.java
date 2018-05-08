package at.tugraz.recipro.recipro;

import java.util.Random;

public class TestUtils {

    public static int getRandomBetween(int lowerBound, int upperBound) {
        Random r = new Random();
        return r.nextInt(upperBound - lowerBound) + lowerBound;
    }
}
