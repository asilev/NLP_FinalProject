package il.ac.openu.nlp.finalproject.models.featuremarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class FeatureMarkerModel {
	public static List<TaggedFeatureVector<String>> buildFeatureMarker(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size()>0) {
					FeatureVector<String> features = new FeatureVector<>(ZERO_INDEX);
					features.put("_numOfMorphemes", getTweetsSize(ZERO_INDEX, tweet));
					features.put("_averageWordSize", getAverageWordSize(ZERO_INDEX, tweet));
					features.put("_numOfPuctMarks", getNumOfPunctuationMarks(ZERO_INDEX, tweet));
					features.put("_avgNumOfPunctMarks", getAverageNumOfPunctuationMarks(ZERO_INDEX, tweet));
					features.put("_posTaggingVb", getPosTagging(ZERO_INDEX, tweet, "VB"));
					features.put("_posTaggingNn", getPosTagging(ZERO_INDEX, tweet, "NN"));
					features.put("_posTaggingJj", getPosTagging(ZERO_INDEX, tweet, "JJ"));
					features.put("_posTaggingPrp", getPosTagging(ZERO_INDEX, tweet, "PRP"));
					features.put("_posTaggingIntj", getPosTagging(ZERO_INDEX, tweet, "INTJ"));
					features.put("_posTaggingCd", getPosTagging(ZERO_INDEX, tweet, "CD"));
					features.put("_averageSentenceSize", getSentenceAvrageSize(ZERO_INDEX, tweet));
					features.put("_numOfLongWords3", getNumOfLongWords(ZERO_INDEX, tweet, 3));
					features.put("_numOfLongWords5", getNumOfLongWords(ZERO_INDEX, tweet, 5));
					features.put("_numOfLongWords7", getNumOfLongWords(ZERO_INDEX, tweet, 7));
					features.put("_numOfLongWords9", getNumOfLongWords(ZERO_INDEX, tweet, 9));
					features.put("_numOfLongWords11", getNumOfLongWords(ZERO_INDEX, tweet, 11));
					features.put("_numOfCharecters", getNumOfCharecters(ZERO_INDEX, tweet));
					
					usersTweetsVector.add(new TaggedFeatureVector<>(features, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
	}

	private static Double getNumOfCharecters(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		Double numOfCharacters = 0.0;
		for (MorphemeRecord morpheme : tweet) {
			numOfCharacters += morpheme.originalWord.length();
		}
		return numOfCharacters;
	}

	private static Double getAverageWordSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		double accSize = 0;
		double numOfWords = 0;
		for (MorphemeRecord morpheme : tweet) {
			accSize += morpheme.originalWord.length();
			numOfWords++;
		}
		return accSize / numOfWords;
	}

	private static Double getNumOfPunctuationMarks(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		double numOfPunctMarks = 0;
		for (MorphemeRecord morpheme : tweet) {
			if (morpheme.isPunctuationMark == true)
				numOfPunctMarks++;
		}
		return numOfPunctMarks;
	}
	
	private static Double getAverageNumOfPunctuationMarks(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		double numOfPunctMarks = 0;
		for (MorphemeRecord morpheme : tweet) {
			if (morpheme.isPunctuationMark == true)
				numOfPunctMarks++;
		}
		return numOfPunctMarks / tweet.size();
	}
	
	private static Double getPosTagging(String ZERO_INDEX, List<MorphemeRecord> tweet, String pos) {
		Integer posCount = 0;
		for (MorphemeRecord morpheme : tweet) {
			String sPos = morpheme.partOfSpeech;
			if (sPos != null && sPos.equals(pos)) {
				posCount++;
			}
		}
		return (double)posCount;
	}
	
	private static Double getTweetsSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		return (double)tweet.size();
	}
	
	private static Double getSentenceAvrageSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		int sentenceSize = 0;
		int totalSentencesSize = 0;
		int numOfSentences = 0;
		boolean isLastPosDot = false;
		for (MorphemeRecord morpheme : tweet) {
			
			if (!morpheme.partOfSpeech.equals("yyDOT")) {
				sentenceSize++;
				isLastPosDot = false;
			}
			else {
				totalSentencesSize += sentenceSize;
				sentenceSize = 0;
				numOfSentences++;
				isLastPosDot = true;
			}
		}
		if (!isLastPosDot) {
			numOfSentences++;
			totalSentencesSize += sentenceSize;
		}
		return ((double) totalSentencesSize / numOfSentences);
	}
	
	private static Double getNumOfLongWords(String ZERO_INDEX, List<MorphemeRecord> tweet, int threshold) {
		double numOfLongWords = 0.0;
		for (MorphemeRecord morpheme : tweet) {
			if (morpheme.originalWord.length() > threshold) {
				++numOfLongWords;
			}
		}
		return numOfLongWords;
	}
}
