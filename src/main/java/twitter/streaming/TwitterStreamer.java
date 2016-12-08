package twitter.streaming;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import util.PropertyReader;

public class TwitterStreamer implements Runnable {
	private List<String> queries;
	private String threadName;
	private KafkaZooKeeperDummy zooKeeper;

	public TwitterStreamer(String threadName, List<String> queries, KafkaZooKeeperDummy zooKeeper) {
		super();
		this.zooKeeper = zooKeeper;
		this.threadName = threadName;
		this.queries = queries;
	}

	@Override
	public void run() {
		Client client = null;
		try {
			Properties props = PropertyReader.get();
			Authentication auth = new OAuth1(
					props.getProperty("twitter.consumer_key"),
					props.getProperty("twitter.consumer_token"),
					props.getProperty("twitter.token_key"),
					props.getProperty("twitter.token_secret")
			);
			StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint()
					.trackTerms(this.queries);
			BlockingQueue<String> twitterStreamQ = new LinkedBlockingQueue<String>();
			endpoint.addPostParameter("language", "en");

			ClientBuilder clientBuilder = new ClientBuilder()
					.hosts(Constants.STREAM_HOST).authentication(auth)
					.endpoint(endpoint)
					.processor(new StringDelimitedProcessor(twitterStreamQ));

			client = clientBuilder.build();
			client.connect();

			int countTweets = 0;
			int maxLimit = Integer.parseInt(PropertyReader.get().getProperty("tweets.maxFetchLimit"));
			while (countTweets < maxLimit) {
				countTweets++;
				String message = twitterStreamQ.take();
				JsonParser p = new JsonParser();
				JsonElement tweetElement = p.parse(message);
				if (tweetElement.isJsonObject()) {
					JsonObject tweet = tweetElement.getAsJsonObject();
					System.out.printf("[%s] Tweet : %s\n", this.threadName,
							tweet.get("text"));
					zooKeeper.getProducer().send(String.join("," ,this.queries), tweet.get("text").getAsString());
				}
			}
		} catch (InterruptedException e) {
			System.out.printf("[%s] Exception :%s", threadName, e.getMessage());
		} finally {
			if (client != null) {
				client.stop();
			}
		}

	}

}
