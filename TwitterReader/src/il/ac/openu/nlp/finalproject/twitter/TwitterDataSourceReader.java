package il.ac.openu.nlp.finalproject.twitter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class TwitterDataSourceReader {
	
	// Path of the file that contains the Twitter texts; the file is in JSON format
	private static String sPath = "C:\\Users\\Ronen Jashek\\eclipse-workspace\\JSONReader\\data\\iw_tweets_20180706040002-20180710040003.json";
	// Path of the file that contains the Twitter texts; the file is in JSON format
	private static String sInputPath;
	private static String sOutputPath;
	
	// The encoding of the Tweets themselves (in our case in Hebrew)
	private static String sEncoding = "UTF8";
	
	// The instance of the JSON parser
	static JSONParser parser = new JSONParser();
	
	// Hash-map used for storing the Tweets and their transliterate counterparts at the end
	static HashMap<String, String> hm = new HashMap<String, String>(); 
	
	// The transliterate table
	static HashMap<String, String> hmTransLiterate = new HashMap<String, String>();
	
	//private static String[] sTwitterUsers = {"zionnenko"};
	private static String[] sTwitterUsers = {"BenCaspit", "NadavEyalDesk", "amit_segal", "AyalaHasson", "grolnik", 
											 "talschneider", "alonbd", "RinoZror", "OrHeller", "baruchikra",
											 "usegal", "kereneubach", "amsterdamski2", "roysharon11", "akivanovick",
											 "sefiova", "YoazHendel1", "Danmargalit", "SivanRahav", "zionnenko"};
	private static String sPunctuationMarks = ".,";
	private static String sCWD = "";
	private static String sOutputDir = "";
	private static int mNumOfTweetsToGet = 1000;

	public static void main(String[] args) {
		
		// Check if we have args
	    if (args.length >=2)
	    {
	    	sInputPath = args[0];
			sOutputPath = args[1];
	    }
		
		sCWD = System.getProperty("user.dir");
		sOutputDir = sCWD + "\\data";
		
		getUserTimeline();
		
		// No need to call the following two methods any longer; the method 'getUserTimeline' reads the Tweets and processes them
		//createTransLiterateHM();		
		//readTwitterFile();
	}
	
	public static void readTwitterFile()
	{
		try {
        	Object obj = null;
        	
        	File inputFile = new File(sInputPath);
        	File outputFile = new File(sOutputPath);


			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), sEncoding));
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), sEncoding));
            // The above line does not compile with Java JDK 1.8.0 - replacing with the below line
			// [Error message is "The constructor BufferedWriter(OutputStreamWriter) is undefined"]
			PrintWriter out = new PrintWriter(new File(sOutputPath), "UTF-8");
    		
            String line;
        	while ((line = in.readLine()) != null) {
        		// process the line
        		obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                
                String text = (String) jsonObject.get("text");
                String tlText = transLiterateString(text);
                String author = (String) ((JSONObject)jsonObject.get("user")).get("screen_name");
                
                out.write("<"+author+"> ");
                out.write(tlText+"\n");                
                
                System.out.println("Original Text: " + text);
                System.out.println("TransLiterated Text: " + tlText);
                System.out.println("Author ID: " + author);
                hm.put(author,  "HEB: [" + text + "] TL: [" + tlText + "]");
        	}

        	in.close();
        	out.close();        	
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void getUserTimeline()
	{
		// Get 1000 Tweets off a user's timeline using paging
		// The list of Twitter users is defined in 'sTwitterUsers'
		// For each user:
		// 1. Write the Tweets 'as is' to a file
		// 2. Normalize the Tweets such that YAP can process them, and write to file
		
		// Configure the twitter4j library and start calling its API's
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("NAr3EwqRB1uFt0k30a9A2F75f")
		  .setOAuthConsumerSecret("WdPxJZIvCFExnevuFouymN3hTog9lRTMDXW3ZSFiM2b9RALMnh")
		  .setOAuthAccessToken("38796469-g7badXPmfTULV5utfvOLNwtv8CbODAuOWz26iL1PP")
		  .setOAuthAccessTokenSecret("OsdoU5VLek1H1ofrUDAHlSQEYc1j6OzHzOuxED5od4wxh");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Paging pg = new Paging();
		
		String sUser = "";
		
		for (int i=0 ; i<sTwitterUsers.length ; i++)
		{
			sUser = sTwitterUsers[i];
			
	        try {
	        	// Prepare the output files - one for the raw Tweets, and one for the normalized ones
	        	String sOutFile = sOutputDir + "\\" + String.format("%02d", i) + "_" + sUser + ".txt";
	        	String sOutNormFile = sOutputDir + "\\" + String.format("%02d", i) + "_" + sUser + "-Normalized.txt";
	        	FileWriter fw = new FileWriter(sOutFile, true);
	            BufferedWriter bw = new BufferedWriter(fw);
	            PrintWriter out = new PrintWriter(bw);
	            
	            // This output file is initialized slightly differently
	            // since the output must be in UTF-8 for YAP
	            PrintWriter out2 = new PrintWriter(new File(sOutNormFile), "UTF-8");
	            
	            for (int j=0 ; j<mNumOfTweetsToGet/200 ; j++)
	            //for (int j=0 ; j<1 ; j++)
	            {
	    			pg.setPage(j+1);
	    			pg.setCount(200);

	            	List<Status> statuses;
		            statuses = twitter.getUserTimeline(sUser, pg);
		            //user = twitter.verifyCredentials().getScreenName();
		            //    statuses = twitter.getUserTimeline();

		            System.out.println("Showing @" + sUser + "'s user timeline. Page #" + (j+1));
		            for (Status status : statuses) {
		            	String sNormTweet = normalizeTweet(status.getText());
		                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
		                out.println(status.toString());
		             
		             // Some Tweets may return empty e.g. if original Tweet was 100% in English, and YAP does not accept empty sections
		                if (sNormTweet.equals("") == false)	
		                	out2.println(sNormTweet + "\n");
		            }
	            }
	            out.close();
	            out2.close();
	        } catch (TwitterException | IOException e) {
	            e.printStackTrace();
	            System.out.println("Failed to get timeline: " + e.getMessage());
	        }
		}
	}
	
	public static String normalizeTweet(String sTweet)
	{
		// Normalize the Tweet text so that it can be processed by YAP
		// Each word or punctuation mark needs to be in its own line
		String sNormalizedTweet = "";
		String[] tokens = sTweet.split(" ");
		
		for (String token : tokens)
		{
			token = processToken(token);	// Tokens may contain mixed English and Hebrew - we need the Hebrew only
			sNormalizedTweet += token + "\n";
		}
		
		sNormalizedTweet = sNormalizedTweet.trim();
		sNormalizedTweet = sNormalizedTweet.replaceAll("\n+", "\n");
		
		return sNormalizedTweet;
	}
	
	public static String processToken(String sToken)
	{
		// Remove any non-Hebrew segments from the token, but keep punctuation and digits
		String sNewToken = "";
		Pattern p = Pattern.compile("\\p{InHebrew}", Pattern.UNICODE_CASE);
		Pattern p2 = Pattern.compile("\\p{Punct}");
		String sRegex = "\\d+";
	    Matcher m = null;
	    Matcher m2 = null;

	    boolean hebrewDetected = false;
	    boolean punctDetected = false;
	    boolean hebrewSectionStarted = false;
	    
	    if (sToken.matches(sRegex))
	    {
	    	sNewToken += sToken + "\n";
	    	return sNewToken;
	    }
	    
	    for (int i = 0; i < sToken.length() ; i++)
	    {
	        String letter = sToken.charAt(i) + "";
	        m = p.matcher(letter);
	        hebrewDetected = m.matches();
	        if (hebrewDetected){
	        	hebrewSectionStarted = true;
	            sNewToken += letter;
	        }
	        if (hebrewSectionStarted)
	        {
	        	m2 = p2.matcher(letter);
	        	punctDetected = m2.matches();
	        	if (punctDetected)
	        	{
	        		if (!letter.equals("\""))	// Add more exclusions here if needed
	        			sNewToken += "\n" + letter;
	        	}
	        }
	    }
	    
	    return sNewToken;
	}
	
	public static void createTransLiterateHM()
	{
		// Hebrew characters
		hmTransLiterate.put("�", "A");
		hmTransLiterate.put("�", "B");
		hmTransLiterate.put("�", "G");
		hmTransLiterate.put("�", "D");
		hmTransLiterate.put("�", "H");
		hmTransLiterate.put("�", "W");
		hmTransLiterate.put("�", "Z");
		hmTransLiterate.put("�", "X");
		hmTransLiterate.put("�", "J");
		hmTransLiterate.put("�", "I");
		hmTransLiterate.put("�", "K");
		hmTransLiterate.put("�", "K");
		hmTransLiterate.put("�", "L");
		hmTransLiterate.put("�", "M");
		hmTransLiterate.put("�", "M");
		hmTransLiterate.put("�", "N");
		hmTransLiterate.put("�", "N");
		hmTransLiterate.put("�", "S");
		hmTransLiterate.put("�", "E");
		hmTransLiterate.put("�", "P");
		hmTransLiterate.put("�", "P");
		hmTransLiterate.put("�", "C");
		hmTransLiterate.put("�", "C");
		hmTransLiterate.put("�", "Q");
		hmTransLiterate.put("�", "R");
		hmTransLiterate.put("�", "F");
		hmTransLiterate.put("�", "T");
		hmTransLiterate.put("\"", "U");
		hmTransLiterate.put(":", "[CLN]");
		hmTransLiterate.put(";", "[SCLN]");
		hmTransLiterate.put(".", "[DOT]");
		hmTransLiterate.put(",", "[CM]");
		hmTransLiterate.put("-", "[DASH]");
		hmTransLiterate.put("\"", "[QUOT]");
		hmTransLiterate.put("?", "[QM]");
		hmTransLiterate.put("!", "[EXCL]");
		hmTransLiterate.put("(", "[LRB]");
		hmTransLiterate.put(")", "[RRB]");
		hmTransLiterate.put("...", "[ELPS]");
		hmTransLiterate.put("'", "[OPOS]");
		hmTransLiterate.put("\n", "[LF]");
		hmTransLiterate.put("&", "[AMPS]");
		hmTransLiterate.put("\\", "[BS]");
		hmTransLiterate.put("/", "[FS]");
		
		// English characters
		hmTransLiterate.put("A", "A");
		hmTransLiterate.put("B", "B");
		hmTransLiterate.put("C", "C");
		hmTransLiterate.put("D", "D");
		hmTransLiterate.put("E", "E");
		hmTransLiterate.put("F", "F");
		hmTransLiterate.put("G", "G");
		hmTransLiterate.put("H", "H");
		hmTransLiterate.put("I", "I");
		hmTransLiterate.put("J", "J");
		hmTransLiterate.put("K", "K");
		hmTransLiterate.put("L", "L");
		hmTransLiterate.put("M", "M");
		hmTransLiterate.put("N", "N");
		hmTransLiterate.put("O", "O");
		hmTransLiterate.put("P", "P");
		hmTransLiterate.put("Q", "Q");
		hmTransLiterate.put("R", "R");
		hmTransLiterate.put("S", "S");
		hmTransLiterate.put("T", "T");
		hmTransLiterate.put("U", "U");
		hmTransLiterate.put("V", "V");
		hmTransLiterate.put("W", "W");
		hmTransLiterate.put("X", "X");
		hmTransLiterate.put("Y", "Y");
		hmTransLiterate.put("Z", "Z");
		
		hmTransLiterate.put("a", "A");
		hmTransLiterate.put("b", "B");
		hmTransLiterate.put("c", "C");
		hmTransLiterate.put("d", "D");
		hmTransLiterate.put("e", "E");
		hmTransLiterate.put("f", "F");
		hmTransLiterate.put("g", "G");
		hmTransLiterate.put("h", "H");
		hmTransLiterate.put("i", "I");
		hmTransLiterate.put("j", "J");
		hmTransLiterate.put("k", "K");
		hmTransLiterate.put("l", "L");
		hmTransLiterate.put("m", "M");
		hmTransLiterate.put("n", "N");
		hmTransLiterate.put("o", "O");
		hmTransLiterate.put("p", "P");
		hmTransLiterate.put("q", "Q");
		hmTransLiterate.put("r", "R");
		hmTransLiterate.put("s", "S");
		hmTransLiterate.put("t", "T");
		hmTransLiterate.put("u", "U");
		hmTransLiterate.put("v", "V");
		hmTransLiterate.put("w", "W");
		hmTransLiterate.put("x", "X");
		hmTransLiterate.put("y", "Y");
		hmTransLiterate.put("z", "Z");
		
		// Digits
		hmTransLiterate.put("1", "1");
		hmTransLiterate.put("2", "2");
		hmTransLiterate.put("3", "3");
		hmTransLiterate.put("4", "4");
		hmTransLiterate.put("5", "5");
		hmTransLiterate.put("6", "6");
		hmTransLiterate.put("7", "7");
		hmTransLiterate.put("8", "8");
		hmTransLiterate.put("9", "9");
		hmTransLiterate.put("0", "0");
		// Other characters
		hmTransLiterate.put("@", "@");
		
		//TODO: Handle linefeed. (see "@ArtsiDraw"), &, ,/ ?... all other nulls. This currently not an issue, since transliterate is not used.
	}
	
	public static String transLiterateString(String s)
	{
		// Takes a string in Hebrew and returns its transliterate
		String sFormattedString = "";
		
		for (int i=0 ; i<s.length() ; i++)
		{
			//char c = s.charAt(s.length()-i-1);
			char c = s.charAt(i);
			if (c != ' ')
			{
				String sTemp = hmTransLiterate.get(""+c);
				sFormattedString += sTemp;
			}
			else { 
				sFormattedString += " ";
			}
		}
		
		return sFormattedString;
	}
	
	public static boolean isSpecialString(String s)
	{
		// Returns true is the string (actually a character) is a special case;
		// If it is, the calling method adds a space before it. This is because 
		// e.g. quotes need to be represent separately 
		boolean isSpecial = false;
		
		if (s == null) {
			return false;
		}
		
		if (s.startsWith("[")) {
			isSpecial = true;
		}
		
		return isSpecial;
	}
}
