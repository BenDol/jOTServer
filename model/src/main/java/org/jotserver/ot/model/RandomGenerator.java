package org.jotserver.ot.model;

import java.util.Random;

public class RandomGenerator {
    private static Random random = new Random();

    public static int range(int from, int to) {
        return random.nextInt(to-from)+from;
    }

}
