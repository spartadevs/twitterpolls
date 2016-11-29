package training;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import java.io.Serializable;

import scala.Tuple2;

public class Trainer implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    private JavaRDD<LabeledPoint> getTrainingData(JavaSparkContext jsc) {
        String path = "src/main/resources/training.1600000.processed.noemoticon.txt";
        return MLUtils.loadLibSVMFile(jsc.sc(), path,-1).toJavaRDD();
    }

    public void train() {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("My App");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        JavaRDD<LabeledPoint>[] tmp = getTrainingData(jsc).randomSplit(new double[]{0.8, 0.2});
        JavaRDD<LabeledPoint> training = tmp[0]; // training set
        JavaRDD<LabeledPoint> test = tmp[1]; // test set
        final NaiveBayesModel model = NaiveBayes.train(training.rdd(), 1.0);
        JavaPairRDD<Double, Double> predictionAndLabel =
                test.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                    private static final long serialVersionUID = 1L;

					@Override
                    public Tuple2<Double, Double> call(LabeledPoint p) {
                        return new Tuple2<>(model.predict(p.features()), p.label());
                    }
                });
        	double accuracy = predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
            private static final long serialVersionUID = 1L;

			@Override
            public Boolean call(Tuple2<Double, Double> pl) {
                return pl._1().equals(pl._2());
            }
        }).count() / (double) test.count();

        System.out.println("accuracy:" + accuracy);
        // Save and load model
        //model.save(jsc.sc(), "target/tmp/myNaiveBayesModel");
        //NaiveBayesModel sameModel = NaiveBayesModel.load(jsc.sc(), "target/tmp/myNaiveBayesModel");
    }

    public static void main(String[] args) {
        (new Trainer()).train();
    }
}
