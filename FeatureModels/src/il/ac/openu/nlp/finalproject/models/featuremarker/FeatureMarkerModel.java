package il.ac.openu.nlp.finalproject.models.featuremarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class FeatureMarkerModel {
	public static List<TaggedFeatureVector<String>> buildFeatureMarker(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size()>0) {
					FeatureVector<String> features = new FeatureVector<>(ZERO_INDEX);
					features.put("numOfMorphemes", getTweetsSize(ZERO_INDEX, tweet));
					
					features.put("averageWordSize", getAverageWordSize(ZERO_INDEX, tweet));
					
					features.put("numOfPuctMarks", getNumOfPunctuationMarks(ZERO_INDEX, tweet));
									
					features.put("avgNumOfPunctMarks", getAverageNumOfPunctuationMarks(ZERO_INDEX, tweet));
					
					features.put("posTagging", getPosTagging(ZERO_INDEX, tweet));
					
					usersTweetsVector.add(new TaggedFeatureVector<>(features, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
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
	
	private static Double getPosTagging(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		Integer posCount = 0;
		for (MorphemeRecord morpheme : tweet) {
			String sPos = morpheme.partOfSpeech;
			if (sPos != null && sPos.equals("VB")) {
				posCount++;
			}
		}
		return (double)posCount;
	}
	
	private static Double getTweetsSize(String ZERO_INDEX, List<MorphemeRecord> tweet) {
		return (double)tweet.size();
	}
}
