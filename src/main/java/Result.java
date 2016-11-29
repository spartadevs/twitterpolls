import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<Double, Integer> score = new HashMap<>();

    public void add(Double predictedClass) {
        if (!score.containsKey(predictedClass)) {
            score.put(predictedClass, 0);
        }

        score.put(predictedClass, score.get(predictedClass) + 1);
    }
}
