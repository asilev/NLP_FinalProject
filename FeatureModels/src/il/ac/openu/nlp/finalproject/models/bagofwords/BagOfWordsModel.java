package il.ac.openu.nlp.finalproject.models.bagofwords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class BagOfWordsModel {
	public static List<TaggedFeatureVector<String>> buildAuthorBagOfWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size()>0) {
					FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
					for (MorphemeRecord morpheme : tweet) {
						String key = "_orig: "+morpheme.originalWord;
						bagOfWords.put(key, bagOfWords.get(key)+1);
					}
					usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
	}

	public static List<TaggedFeatureVector<String>> buildAuthorBagOfStemmedWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size()>0) {
					FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
					for (MorphemeRecord morpheme : tweet) {
						String key = "_stemmed: "+morpheme.stemmedWord;
						bagOfWords.put(key, bagOfWords.get(key)+1);
					}
					usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
	}
	
	public static List<TaggedFeatureVector<String>> buildAuthorBagOf2GramsWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size()>0) {
					FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
					bagOfWords.put("_bigram: <SOS> "+tweet.get(0).originalWord, 1.0);
					for (int i=0; i<tweet.size()-1; ++i) {
						String _2gram = "_bigram: "+tweet.get(i).originalWord+" "+tweet.get(i+1).originalWord;
						bagOfWords.put(_2gram, bagOfWords.get(_2gram)+1);
					}
					bagOfWords.put("_bigram: "+tweet.get(tweet.size()-1).originalWord+" <EOS>", 1.0);
					usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
				}
			}
		}
		return usersTweetsVector;
	}
	
	public static List<TaggedFeatureVector<String>> buildAuthorBagOfPosWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			for (List<MorphemeRecord> tweet : user.getValue()) {
				FeatureVector<String> bagOfPosWords = new FeatureVector<String>(ZERO_INDEX);
				for (MorphemeRecord morpheme : tweet) {
					String key = "_pos: "+morpheme.originalWord+" "+morpheme.partOfSpeech;
					bagOfPosWords.put(key, bagOfPosWords.get(key)+1);
				}
				usersTweetsVector.add(new TaggedFeatureVector<>(bagOfPosWords, user.getKey()));
			}
		}
		return usersTweetsVector;
		
	}

	public static List<TaggedFeatureVector<String>> buildAuthorGlobalBagOfWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			for (List<MorphemeRecord> tweet : user.getValue()) {
				for (MorphemeRecord morpheme : tweet) {
					bagOfWords.put(morpheme.originalWord, bagOfWords.get(morpheme.originalWord)+1);
				}
			}
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
		}
		return usersTweetsVector;
	}

	public static List<TaggedFeatureVector<String>> buildAuthorGlobalStemmedBagOfWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			for (List<MorphemeRecord> tweet : user.getValue()) {
				for (MorphemeRecord morpheme : tweet) {
					bagOfWords.put(morpheme.stemmedWord, bagOfWords.get(morpheme.stemmedWord)+1);
				}
			}
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
		}
		return usersTweetsVector;
	}

	public static List<TaggedFeatureVector<String>> buildAuthorBagOfGlobal2GramsWords(Map<String, List<List<MorphemeRecord>>> userTweets, String ZERO_INDEX) {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<List<MorphemeRecord>>> user : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			for (List<MorphemeRecord> tweet : user.getValue()) {
				if (tweet.size() > 0) {
					bagOfWords.put("<SOS> "+tweet.get(0).originalWord, 1.0);
					for (int i=0; i<tweet.size()-1; ++i) {
						String _2gram = tweet.get(i).originalWord+" "+tweet.get(i+1).originalWord;
						bagOfWords.put(_2gram, bagOfWords.get(_2gram)+1);
					}
					bagOfWords.put(tweet.get(tweet.size()-1).originalWord+" <EOS>", 1.0);
				}
			}
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, user.getKey()));
		}
		return usersTweetsVector;
	}
}