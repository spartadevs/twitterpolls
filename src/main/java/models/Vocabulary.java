package models;

import scala.util.parsing.combinator.testing.Str;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Vocabulary implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String,Integer> indexMap;
	private Integer vocabIndex;
	
	public Vocabulary(){
		indexMap = Collections.synchronizedMap(new HashMap<String,Integer>());
		vocabIndex = 0;
	}
	
	
	public void setIndexMap(Map<String, Integer> indexMap) {
		this.indexMap = indexMap;
	}
	
	public Map<String, Integer> getIndexMap() {
		return indexMap;
	}

	public Integer addToVocabulary(String word){
		Integer index = getWordIndex(word);
		if(index == -1){
			word = cleanWord(word);
			index = ++vocabIndex;
			indexMap.put(word, index);
		}

		return index;
	}
	
	public Integer getWordIndex(String word){
		Integer index = -1;
		String cleanedWord = cleanWord(word);

		if(!cleanedWord.equals("")){
			if(indexMap.containsKey(cleanedWord)){
				index = indexMap.get(cleanedWord);
			}
		}

		return index;
	}

	private String cleanWord(String word) {
		if (word!=null) {
			return word.trim();
		}

		return "";
	}

	public int getSize() {
		return vocabIndex;
	}
}
