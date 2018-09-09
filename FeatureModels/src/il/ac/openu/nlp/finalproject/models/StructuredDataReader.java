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

public class StructuredDataReader {
	// This class handles the YAP output files
	
	// Hash from author to a list of sentences. Each sentence is a list of morphemes.
//	private Map<String, List<MorphemeRecord>> userTweets;
//	private boolean bPunctDetected = false;
	private File[] listOfFiles = null;
//	private List<TaggedFeatureVector<String>> usersTweets;
	private String encoding;
	private Set<String> listOfUniqueWords = new HashSet<>();
	
	public StructuredDataReader(String structuredDataPath, String encoding)
	{
		File folder = new File(structuredDataPath);
		listOfFiles = folder.listFiles();
		this.encoding = encoding;
	}
	
	// Return value is map from author to a list of sentences (tweets). Each sentence is a list of morphemes.
	public Map<String, List<List<MorphemeRecord>>> readStructuredData(String extention, int numOfAuthors) throws IOException
	{
		Map<String, List<List<MorphemeRecord>>> userTweets = new HashMap<>();
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().endsWith("."+extention)) {
		        String sName = file.getName();
		        String[] tokens = sName.split("_");
		        String currUserName = tokens[0];
		        if (Integer.parseInt(currUserName)>=numOfAuthors) {
		        	return userTweets;
		        }
		        System.out.println("Processing the file: " + file.getName());
		        List<MorphemeRecord> tweet = new ArrayList<>();
		        BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
				String str;
		        while ((str = in.readLine()) != null) {
		        	if (str.equals("") == false)
		        	{
		        		MorphemeRecord morpheme = new MorphemeRecord();
			        	tokens = str.split("\t");
//			        	String sWord = "";
			        	morpheme.originalWord = tokens[2];
			        	morpheme.stemmedWord = tokens[3];
			        	morpheme.partOfSpeech = tokens[4];
			        	if (tokens[4].startsWith("yy")) {
			        		morpheme.isPunctuationMark = true;
			        	}
//				        listOfUniqueWords.add(sWord);
			        	// Check if the token is a punctuation mark
//			        	m = p.matcher(sWord);
//			        	bPunctDetected = m.matches();
//		        		morpheme.isPunctuationMark = bPunctDetected;
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
		        		
		        		List<List<MorphemeRecord>> tweets = userTweets.get(currUserName);
		        		if (tweets == null) {
		        			tweets = new ArrayList<>();
		        			userTweets.put(currUserName, tweets);
		        		}
		        		tweets.add(tweet);
				        tweet = new ArrayList<>();
		        	}
		        }
			        
		        // Completed processing all tweets for one user; close the file
		        in.close();
		    }
		}
		return userTweets;
	}

	public Set<String> getListOfUniqueWords() {
		return listOfUniqueWords;
	}
}