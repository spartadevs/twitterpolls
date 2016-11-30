package twitter.streaming;

import models.Result;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import training.Trainer;
import util.ModelUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Driver {
	private static void train() {
		NaiveBayesModel model = (new Trainer()).train();
		ModelUtil.save(null, model);
	}

	private static Result test(String[] brands) {
		ExecutorService execService = null;
		int params = 0;
		KafkaZooKeeperDummy kafkaZooKeeper = new KafkaZooKeeperDummy();
		if (brands != null) {
			params = brands.length;
			int counter = 0;
			execService = Executors.newFixedThreadPool(params);
			while (counter < params) {
				List<String> queryString = Arrays.asList(brands[counter].split("-"));
				TwitterStreamer streamer = new TwitterStreamer("Thread"+ counter, queryString, kafkaZooKeeper);
				execService.execute(streamer);
				counter++;
			}
		}
		execService.shutdown();
		try {
			execService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return kafkaZooKeeper.getConsumer().getResult();
	}


	public static void main(String[] args) {
		train();
		Result result = test(args);
		result.print();
	}
}
