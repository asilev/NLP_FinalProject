package il.ac.openu.nlp.finalproject.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructuredDataReader {
	// This class handles the YAP output files
	
	// Hash from author to a list of sentences. Each sentence is a list of morphemes.
	private Map<String, List<MorphemeRecord>> userTweets;
	private boolean bFilterPunctEnabled = true;
	private boolean bStemmingEnabled = false;
	private boolean bPunctDetected = false;
	private File[] listOfFiles = null;
//	private List<TaggedFeatureVector<String>> usersTweets;
	private String encoding;
	private Set<String> listOfUniqueWords = new HashSet<>();
	
	public StructuredDataReader(String structuredDataPath, String encoding)
	{
		File folder = new File(structuredDataPath);
		listOfFiles = folder.listFiles();
		this.encoding = encoding;
		userTweets = new HashMap<>();
	}
	
	private void buildUserWordCountMap() throws IOException
	{
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println("Processing the file: " + file.getName());
		        String sName = file.getName();
		        String[] tokens = sName.split("_");
		        String currUserName = tokens[1];
		        List<MorphemeRecord> tweet = new ArrayList<>();
		        BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
				Pattern p = Pattern.compile("\\p{Punct}");
			    Matcher m = null;
					
				String str;
		        while ((str = in.readLine()) != null) {
		        	if (str.equals("") == false)
		        	{
		        		MorphemeRecord morpheme = new MorphemeRecord();
			        	tokens = str.split("\t");
			        	String sWord = "";
			        	morpheme.originalWord = tokens[2];
			        	morpheme.stemmedWord = tokens[3];
				        listOfUniqueWords.add(sWord);
			        	// Check if the token is a punctuation mark
			        	m = p.matcher(sWord);
			        	bPunctDetected = m.matches();
		        		morpheme.isPunctuationMark = bPunctDetected;
		        		// The word exists in the master word map 
//		        		if (bFilterPunctEnabled == false || (bFilterPunctEnabled == true && bPunctDetected == false))
//		        		{
//		        			newTweet.getFeatureVector().put(sWord, newTweet.getFeatureVector().get(sWord)+1);
//		        		}
		        		tweet.add(morpheme);
		        	}
		        	else
		        	{
		        		// An empty line indicates we completed processing a tweet,
		        		// now we can add it to the user's list of tweets and prepare
		        		// for the next tweet
		        		userTweets.put(currUserName, tweet);
				        tweet = new ArrayList<>();
		        	}
		        }
			        
		        // Completed processing all tweets for one user; close the file
		        in.close();
		    }
		}
	}

	public List<TaggedFeatureVector<String>> buildAuthorBagOfWords(String ZERO_INDEX) throws IOException {
		buildUserWordCountMap();
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

	public List<TaggedFeatureVector<String>> buildAuthorBagOfStemmedWords(String ZERO_INDEX) throws IOException {
		buildUserWordCountMap();
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
	
	public List<TaggedFeatureVector<String>> buildAuthorBagOf2GramsWords(String ZERO_INDEX) throws IOException {
		buildUserWordCountMap();
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

	
	public Set<String> getListOfUniqueWords() {
		return listOfUniqueWords;
	}
}