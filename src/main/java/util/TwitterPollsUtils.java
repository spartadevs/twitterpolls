package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import com.google.common.primitives.*;
import models.Vocabulary;

import models.Vocabulary;
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
				word = word.replaceAll("[^A-Za-z0-9]","");
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

		//Generate word-frequency map
		HashMap<String, Integer> tweetDict = TwitterPollsUtils
				.getCleanedTweetDictionary(tweet);
		TreeMap<Integer,Double> indexDict = new TreeMap<Integer,Double>();
		
		//Generate index-value map
		for (String key : tweetDict.keySet()) {
			int wIdx = vocab.addToVocabulary(key);
			if(wIdx!=-1){
				indexDict.put(wIdx, tweetDict.get(key).doubleValue());
			}
		}
		// Preparing a Sparse Vector
		int dictLen = indexDict.size();
		Vector features = Vectors.sparse(dictLen, Ints.toArray(indexDict.keySet()), Doubles.toArray(indexDict.values()));
		return features;

	}
	
	public static String generateVectorString(String tweet, Vocabulary vocab) {

		StringBuffer sb = new StringBuffer();
		//Generate word-frequency map
		HashMap<String, Integer> tweetDict = TwitterPollsUtils
				.getCleanedTweetDictionary(tweet);
		TreeMap<Integer,Integer> indexDict = new TreeMap<Integer,Integer>();
		
		//Generate index-value map
		for (String key : tweetDict.keySet()) {
			int wIdx = vocab.addToVocabulary(key);
			if(wIdx!=-1){
				//System.out.println(key + wIdx);
				indexDict.put(wIdx, tweetDict.get(key));
			}
		}
		//Preparing SVM Format String
		for(Integer wordIndex : indexDict.keySet()){
				double wordFreq = indexDict.get(wordIndex);
				sb.append(" ").append(wordIndex).append(":").append(wordFreq);
			}
		return sb.toString();

	}

	public static Vocabulary loadVocabulary (String filePath)throws IOException,ClassNotFoundException{
		Vocabulary vocab = null;
		try (FileInputStream fin = new FileInputStream(filePath);
				 ObjectInputStream out = new ObjectInputStream(fin);){
					vocab = (Vocabulary)out.readObject();
				}catch(Exception e){
					System.err.println("[Error][loadVocabulary]:"+e.getMessage());
					throw e;
				}
		return vocab;
		}


	public static void dumpVocabulary(Vocabulary vocab, String filePath)throws IOException{
		try (FileOutputStream fout = new FileOutputStream(filePath);
			 ObjectOutputStream out = new ObjectOutputStream(fout);){
				out.writeObject(vocab);
				out.flush();
			}catch(Exception e){
				System.err.println("[Error][dumpVocabulary]:"+e.getMessage());
				throw e;
			}
	}
}
