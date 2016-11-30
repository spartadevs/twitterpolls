package util;

import org.apache.spark.SparkContext;
import org.apache.spark.mllib.classification.NaiveBayesModel;

public class ModelUtil {
    private static NaiveBayesModel __model = null;

    private static String getStorageLocation() {
        return PropertyReader.get().getProperty("model.persistence.location");
    }

    public static void save(SparkContext sc, NaiveBayesModel model) {
        __model = model;
//        model.save(sc, getStorageLocation());
    }

    public static NaiveBayesModel getModel() {
        return __model;
    }

    private static NaiveBayesModel load(SparkContext sc) {
        return NaiveBayesModel.load(sc, getStorageLocation());
    }
}
