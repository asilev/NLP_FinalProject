package il.ac.openu.nlp.finalproject.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StructuredDataReader { //TODO: user feature vector and classified feature vector data types as return values. 
	// This class handles the YAP output files
	
	private boolean bFilterPunctEnabled = true;
	private boolean bStemmingEnabled = false;
	private boolean bPunctDetected = false;
	private static Map<String, Integer> masterWordsIndex = new HashMap<>();
	private static Map<String, Integer> masterUsersIndex = new HashMap<>();
	private static Integer mWordsID = 0;
	private static Integer mNumofUsers = 0;
	private static File[] listOfFiles = null;
	private static final int NUMOFUSERS = 20;
	private static int[] tweetsPerUser = new int[NUMOFUSERS];
	private Map<String, Integer> tweetWordCount;
	private SDStructure[] usersTweets;
	
	// An interim data structure (until integration
	// with Asi's code) for storing Tweets word
	// counts for each user
	public class SDStructure{
		private String userName;
		private Integer userID;
		private List<Map> listOfTweetsWordCount;
		
		public SDStructure()
		{
			this.userName = "";
			this.userID = -1;
			this.listOfTweetsWordCount = new ArrayList();
		}
		
		public String getUserName()
		{
			return this.userName;
		}
		
		public Integer getUserID()
		{
			return this.userID;
		}
	}
	
	public StructuredDataReader(String structuredDataFilename)
	{
		File folder = new File(structuredDataFilename);
		//"C:\\Users\\Ronen Jashek\\git\\NLP_FinalProject\\TwitterReader\\OutputFromYap"
		listOfFiles = folder.listFiles();
	}
	
	public void buildMasterWordIndex() throws IOException
	{
		// Go over all user's files and create one master
		// map that contains all words found in the tweets,
		// and for each word give an ID (a running index)
		int mNumTweetsPerUser = 0;
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println("Processing the file: " + file.getName());
		        String sName = file.getName();
		        String[] tokens = sName.split("_");
		        int currUserID = Integer.parseInt(tokens[0]);
		        masterUsersIndex.put(tokens[1], currUserID);
		        mNumofUsers++;
		        
		        BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
				Pattern p = Pattern.compile("\\p{Punct}");
			    Matcher m = null;
				
				String str;
		        while ((str = in.readLine()) != null) {
		        	if (str.equals("") == false)
		        	{
		        		tokens = str.split("\t");
		        		String sWord = "";
			        	
		        		if (bStemmingEnabled == false)
		        			sWord = tokens[2];
		        		else
		        			sWord = tokens[3];
			        	
		        		// Check if the token is a punctuation mark
		        		m = p.matcher(sWord);
		        		bPunctDetected = m.matches();
		        		if (masterWordsIndex.get(sWord) == null)
		        		{
		        			// The word does not exist in the map yet
		        			if (bFilterPunctEnabled == false || (bFilterPunctEnabled == true && bPunctDetected == false))
		        			{
		        				masterWordsIndex.put(sWord,  mWordsID);
		        				mWordsID++;
		        			}
		        		}
		        		else
		        			System.out.println("The word " + sWord + " not added to the map, already exists.");
		        	}
		        	else
		        		mNumTweetsPerUser++;
			    }
			        
		       	tweetsPerUser[currUserID] = mNumTweetsPerUser;
		       	mNumTweetsPerUser = 0;
		       	in.close();

		    }
		}
		System.out.println("Completed processing all files. The size of the map is: " + masterWordsIndex.size());
	}
	
	public void buildUserWordCountMap() throws IOException
	{
        usersTweets = new SDStructure[mNumofUsers];

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println("Processing the file: " + file.getName());
		        String sName = file.getName();
		        String[] tokens = sName.split("_");
		        int currUserID = Integer.parseInt(tokens[0]);
		        String currUserName = tokens[1];
		        
		        usersTweets[currUserID] = new SDStructure();
		        usersTweets[currUserID].userID = currUserID;
		        usersTweets[currUserID].userName = currUserName;
		        
		        BufferedReader in;
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
				Pattern p = Pattern.compile("\\p{Punct}");
			    Matcher m = null;
					
				String str;
				tweetWordCount = new HashMap<>();
		        while ((str = in.readLine()) != null) {
		        	if (str.equals("") == false)
		        	{
			        	tokens = str.split("\t");
			        	String sWord = "";
			        	
			        	if (bStemmingEnabled == false)
			        		sWord = tokens[2];
			        	else
			        		sWord = tokens[3];
				        	
			        	// Check if the token is a punctuation mark
			        	m = p.matcher(sWord);
			        	bPunctDetected = m.matches();
			        	if (masterWordsIndex.get(sWord) != null)
			        	{
			        		// The word exists in the master word map 
			        		if (bFilterPunctEnabled == false || (bFilterPunctEnabled == true && bPunctDetected == false))
			        		{
			        			if (tweetWordCount.containsKey(sWord))
			        			{
			        				// If the word already exists in the
			        				// current tweet, increment its count
			        				int count = tweetWordCount.get(sWord);
			        	            tweetWordCount.put(sWord, count + 1);
			        			}
			        			else
			        			{
			        				// Word was not encountered before,
			        				// set its count to 1
			        				tweetWordCount.put(sWord, 1);
			        			}
			        		}
			        	}
			        	else
			        		System.out.println("The word " + sWord + " was not counted since it's punctuation.");
		        	}
		        	else
		        	{
		        		// An empty line indicates we completed processing a tweet,
		        		// now we can add it to the user's list of tweets and prepare
		        		// for the next tweet
		        		usersTweets[currUserID].listOfTweetsWordCount.add(tweetWordCount);
		        		tweetWordCount = new HashMap<>();
		        	}
		        }
			        
		        // Completed processing all tweets for one user; close the file
		        in.close();
		    }
		}
	}
}
