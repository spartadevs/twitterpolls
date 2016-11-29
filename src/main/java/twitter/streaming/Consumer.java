package twitter.streaming;

import models.testing.Result;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.linalg.Vector;
import util.ModelUtil;
import util.PropertyReader;
import util.TwitterPollsUtils;

public class Consumer {
    public void consume(String brand, String tweet) {
        Vector vectorizedTweet = vectorize(tweet);
//        // TODO: 11/28/2016 see if it is ok to create new spark context
        SparkConf conf = new SparkConf().setMaster("local").setAppName(PropertyReader.get().getProperty("spark.app.name"));
        JavaSparkContext jsc = new JavaSparkContext(conf);
        double label = ModelUtil.getInstance(jsc.sc()).getModel().predict(vectorizedTweet);
        Result.add(label);
    }

    private Vector vectorize(String text) {
//        // TODO: 11/28/2016 pass vocabulary
        return TwitterPollsUtils.vectorizeTweet(text, null);
    }
}
