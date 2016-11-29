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

    private static void printScore(Map<Double, Integer> labelWiseScore) {
        int total = 0;
        for (Map.Entry<Double, Integer> entry : labelWiseScore.entrySet()) {
            total += entry.getValue();
//            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        System.out.println("Label wise percentages:");
        for (Map.Entry<Double, Integer> entry : labelWiseScore.entrySet()) {
            System.out.println(entry.getKey() + "\t:\t" + ((double)entry.getValue()/total)*100);
        }
    }

    public static void printBrandScore(String brand) {
        System.out.println(brand);
        printScore(brandWiseResults.get(brand));
    }

    public static void print() {
        for (Map.Entry<String, Map<Double, Integer>> entry : brandWiseResults.entrySet())
        {
            printBrandScore(entry.getKey());
        }
    }
}
