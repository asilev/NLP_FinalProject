package il.ac.openu.nlp.finalproject.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructuredDataReader {
	// TODO: This should be static class
	// This class handles the YAP output files
	
	private boolean bFilterPunctEnabled = true;
	private boolean bStemmingEnabled = false;
	private boolean bPunctDetected = false;
	private File[] listOfFiles = null;
	private List<TaggedFeatureVector<String>> usersTweets;
	private String encoding;
	private Set<String> listOfUniqueWords = new HashSet<>();
	
	public StructuredDataReader(String structuredDataPath, String encoding)
	{
		File folder = new File(structuredDataPath);
		//"C:\\Users\\Ronen Jashek\\git\\NLP_FinalProject\\TwitterReader\\OutputFromYap"
		listOfFiles = folder.listFiles();
		this.encoding = encoding;
	}
	
	public void buildUserWordCountMap() throws IOException
	{
		usersTweets = new ArrayList<TaggedFeatureVector<String>>();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println("Processing the file: " + file.getName());
		        String sName = file.getName();
		        String[] tokens = sName.split("_");
		        String currUserName = tokens[1];
		        TaggedFeatureVector<String> newTweet = new TaggedFeatureVector<>(currUserName);
		        
		        BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
				Pattern p = Pattern.compile("\\p{Punct}");
			    Matcher m = null;
					
				String str;
		        while ((str = in.readLine()) != null) {
		        	if (str.equals("") == false)
		        	{
			        	tokens = str.split("\t");
			        	String sWord = "";
			        	
			        	if (bStemmingEnabled == false) {
			        		sWord = tokens[2];
			        	}
			        	else {
			        		sWord = tokens[3];
			        	}
				        listOfUniqueWords.add(sWord);
			        	// Check if the token is a punctuation mark
			        	m = p.matcher(sWord);
			        	bPunctDetected = m.matches();
		        		// The word exists in the master word map 
		        		if (bFilterPunctEnabled == false || (bFilterPunctEnabled == true && bPunctDetected == false))
		        		{
		        			newTweet.getFeatureVector().put(sWord, newTweet.getFeatureVector().get(sWord)+1);
		        		}
		        	}
		        	else
		        	{
		        		// An empty line indicates we completed processing a tweet,
		        		// now we can add it to the user's list of tweets and prepare
		        		// for the next tweet
		        		usersTweets.add(newTweet);
				        newTweet = new TaggedFeatureVector<>(currUserName);
		        	}
		        }
			        
		        // Completed processing all tweets for one user; close the file
		        in.close();
		    }
		}
	}

	public List<TaggedFeatureVector<String>> buildAuthorBagOfWords() throws IOException {
		buildUserWordCountMap();
		return usersTweets;
	}
	
	public Set<String> getListOfUniqueWords() {
		return listOfUniqueWords;
	}
}