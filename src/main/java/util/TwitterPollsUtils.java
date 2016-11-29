package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class TwitterPollsUtils {

	/*
	 * public static Set<String> ignoreList; static{ String strIgnoreList =
	 * PropertyReader.getValue("IGNORE_WORD_LIST"); if(strIgnoreList!=null){
	 * ignoreList = new
	 * HashSet<String>(Arrays.asList(strIgnoreList.split("-"))); } }
	 */

	public static String cleanTweetWord(String word) {
		String result = null;
		if (null != word && !word.trim().isEmpty()) {
			if (!word.startsWith("@") && !word.startsWith("http")) {
				word = word.replaceAll("[^A-Za-z]+", "");
				/*
				 * if(!ignoreList.contains(word.toUpperCase())) {}
				 */

				result = word.toLowerCase();

			}
		}
		return result;
	}

	public static HashMap<String, Integer> getCleanedTweetDictionary(
			String tweet) {
		List<String> words = null;
		HashMap<String, Integer> tweetDict = new HashMap<String, Integer>();
		if (tweet != null) {
			words = Arrays.asList(tweet.split(" "));
			for (String w : words) {
				String cleaned = cleanTweetWord(w);
				if (tweetDict.containsKey(cleaned)) {
					Integer wCnt = tweetDict.get(cleaned);
					tweetDict.replace(cleaned, wCnt + 1);
				}

				else {
					tweetDict.put(cleaned, 1);
				}
			}
		}
		return tweetDict;
	}

	public static Vector vectorizeTweet(String tweet, Vocabulary vocab) {

		HashMap<String, Integer> tweetDict = TwitterPollsUtils
				.getCleanedTweetDictionary(tweet);
		int[] wIdx = new int[tweetDict.size()];
		double[] wCnt = new double[tweetDict.size()];
		int idx = 0;
		for (String key : tweetDict.keySet()) {
			wIdx[idx] = vocab.addToVocabulary(key);
			wCnt[idx] = tweetDict.get(key).doubleValue();
			idx++;
		}
		// Preparing a Labelled Point
		Vector features = Vectors.sparse(idx, wIdx, wCnt);
		return features;

	}
	
	public static Vocabulary loadVocabulary(String filePath){
		Vocabulary vocab = new Vocabulary();
		return vocab;
	}
}
