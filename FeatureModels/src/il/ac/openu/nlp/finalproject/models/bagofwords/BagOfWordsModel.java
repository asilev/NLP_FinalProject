package il.ac.openu.nlp.finalproject.models.bagofwords;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import il.ac.openu.nlp.finalproject.models.FeatureVector;
import il.ac.openu.nlp.finalproject.models.MorphemeRecord;
import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class BagOfWordsModel {
	private static final String ZERO_INDEX = "Zero";
	private static StructuredDataReader dataReader;
	private static List<TaggedFeatureVector<String>> taggedFeatureVectorList;
	
	public List<TaggedFeatureVector<String>> buildAuthorBagOfWords(Map<String, List<MorphemeRecord>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<MorphemeRecord>> tweet : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			for (MorphemeRecord morpheme : tweet.getValue()) {
				bagOfWords.put(morpheme.originalWord, bagOfWords.get(morpheme)+1);
			}
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, tweet.getKey()));
		}
		return usersTweetsVector;
	}

	public List<TaggedFeatureVector<String>> buildAuthorBagOfStemmedWords(Map<String, List<MorphemeRecord>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<MorphemeRecord>> tweet : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			for (MorphemeRecord morpheme : tweet.getValue()) {
				bagOfWords.put(morpheme.stemmedWord, bagOfWords.get(morpheme)+1);
			}
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, tweet.getKey()));
		}
		return usersTweetsVector;
	}
	
	public List<TaggedFeatureVector<String>> buildAuthorBagOf2GramsWords(Map<String, List<MorphemeRecord>> userTweets, String ZERO_INDEX) throws IOException {
		List<TaggedFeatureVector<String>> usersTweetsVector = new ArrayList<>();
		for (Map.Entry<String, List<MorphemeRecord>> tweet : userTweets.entrySet()) {
			FeatureVector<String> bagOfWords = new FeatureVector<String>(ZERO_INDEX);
			bagOfWords.put("SOS "+tweet.getValue().get(0), 1.0);
			for (int i=0; i<tweet.getValue().size()-1; ++i) {
				String _2gram = tweet.getValue().get(i)+" "+tweet.getValue().get(i+1);
				bagOfWords.put(_2gram, bagOfWords.get(_2gram)+1);
			}
			bagOfWords.put(tweet.getValue().get(tweet.getValue().size()-1)+" EOS", 1.0);
			usersTweetsVector.add(new TaggedFeatureVector<>(bagOfWords, tweet.getKey()));
		}
		return usersTweetsVector;
	}

	public static void main(String[] args) throws IOException {
		if (args.length>0) {
			if (args.length==1) {
				dataReader = new StructuredDataReader(args[0], "UTF8");
			}
			else if (args.length > 1) {
				dataReader = new StructuredDataReader(args[0], args[1]);
			}
		}
		else {
			printUsage();
			System.exit(-1);
		}
		BagOfWordsModel bow = new BagOfWordsModel();
		Map<String,List<MorphemeRecord>> tweets = dataReader.readStructuredData();
		taggedFeatureVectorList = bow.buildAuthorBagOfWords(tweets, ZERO_INDEX);
	}

	private static void printUsage() {
		System.out.println("Usage: BagOfWordsModel <output from yac folder> <encoding(=UTF8)>");
	}
}