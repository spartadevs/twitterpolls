package twitter.streaming;

import models.Result;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import util.ModelUtil;
import util.PropertyReader;
import util.TwitterPollsUtils;
import models.Vocabulary;

import java.io.IOException;

public class Consumer {
    private static Consumer __consumer = null;
    private Vocabulary vocabulary = null;
    private Consumer() {
        try {
            vocabulary = TwitterPollsUtils.loadVocabulary(PropertyReader.get().getProperty("vocabulary.persistence.location"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("failed to load vocabulary");
        }
    }

    public void consume(String brand, String tweet) {
        Vector vectorizedTweet = vectorize(tweet);
//        // TODO: 11/28/2016 see if it is ok to create new spark context
        SparkConf conf = new SparkConf().setMaster("local").setAppName(PropertyReader.get().getProperty("spark.app.name"));
        JavaSparkContext jsc = new JavaSparkContext(conf);
        double label = ModelUtil.getInstance(jsc.sc()).getModel().predict(vectorizedTweet);
        Result.add(brand, label);
    }

    private Vector vectorize(String text) {
//        // TODO: 11/28/2016 pass vocabulary
        return TwitterPollsUtils.vectorizeTweet(text, vocabulary);
    }

    public static Consumer getInstance() {
        if (__consumer==null) {
            __consumer = new Consumer();
        }

        return __consumer;
    }
}
