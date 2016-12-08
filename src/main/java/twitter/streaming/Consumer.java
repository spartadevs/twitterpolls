package twitter.streaming;

import models.Result;
import models.Vocabulary;
import org.apache.spark.mllib.linalg.Vector;
import util.ModelUtil;
import util.PropertyReader;
import util.TwitterPollsUtils;

import java.io.IOException;

public class Consumer {
    private Vocabulary vocabulary = null;
    private Result result = new Result();

    public Consumer() {
        try {
            vocabulary = TwitterPollsUtils.loadVocabulary(PropertyReader.get().getProperty("vocabulary.persistence.location"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("failed to load vocabulary file\n" + e);
        }
    }

    public Result getResult() {
        return result;
    }

    public void consume(String brand, String tweet) {
        Vector vectorizedTweet = vectorize(tweet);
//        // TODO: 11/28/2016 see if it is ok to create new spark context
//        SparkConf conf = new SparkConf().setMaster("local").setAppName(PropertyReader.get().getProperty("spark.app.name"));
//        JavaSparkContext jsc = new JavaSparkContext(conf);
        double label = ModelUtil.getModel().predict(vectorizedTweet);
        result.add(brand, label);
    }

    private Vector vectorize(String text) {
//        // TODO: 11/28/2016 pass vocabulary.dat
        return TwitterPollsUtils.vectorizeTweet(text, vocabulary, false);
    }
}
