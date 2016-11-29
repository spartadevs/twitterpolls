package util;

import org.apache.spark.SparkContext;
import org.apache.spark.mllib.classification.NaiveBayesModel;

public class ModelUtil {
    private static ModelUtil cached = null;
    private NaiveBayesModel __model = null;

    private ModelUtil(SparkContext sc) {
        __model = load(sc);
    }

    public static ModelUtil getInstance(SparkContext sc) {
        if (cached == null) {
            cached = new ModelUtil(sc);
        }

        return cached;
    }

    private static String getStorageLocation() {
        return PropertyReader.get().getProperty("model.persistence.location");
    }

    public static void save(SparkContext sc, NaiveBayesModel model) {
        model.save(sc, getStorageLocation());
    }

    public NaiveBayesModel getModel() {
        return __model;
    }

    private NaiveBayesModel load(SparkContext sc) {
        return NaiveBayesModel.load(sc, getStorageLocation());
    }
}
