package models;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private static Map<String, Map<Double, Integer>> brandWiseResults = new HashMap<>();

    public static void add(String brand, double predictedClass) {
        if (!brandWiseResults.containsKey(brand)) {
            brandWiseResults.put(brand, new HashMap<>());
        }
        Map<Double, Integer> score = brandWiseResults.get(brand);

        if (!score.containsKey(predictedClass)) {
            score.put(predictedClass, 0);
        }

        score.put(predictedClass, score.get(predictedClass) + 1);
    }
}
