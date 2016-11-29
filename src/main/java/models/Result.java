package models;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private static Map<Double, Integer> score = new HashMap<>();

    public static void add(double predictedClass) {
        if (!score.containsKey(predictedClass)) {
            score.put(predictedClass, 0);
        }

        score.put(predictedClass, score.get(predictedClass) + 1);
    }
}
