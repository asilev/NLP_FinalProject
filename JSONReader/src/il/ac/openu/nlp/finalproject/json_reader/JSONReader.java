package il.ac.openu.nlp.finalproject.json_reader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONReader {
	
	// Path of the file that contains the Twitter texts; the file is in JSON format
	private static String sPath;
	
	// The encoding of the Twitts themselves (in our case in Hebrew)
	private static String sEncoding = "UTF8";
	
	// The instance of the JSON parser
	static JSONParser parser = new JSONParser();
	
	// Hash-map used for storing the Twitts and their transliterate counterparts at the end
	static HashMap<String, String> hm = new HashMap<String, String>(); 
	
	// The transliterate table
	static HashMap<String, String> hmTransLiterate = new HashMap<String, String>();

	public static void main(String[] args) {
		sPath = args[0];
		createTransLiterateHM();		
		readTwitterFile();
	}
	
	public static void readTwitterFile()
	{
		try {
        	Object obj = null;
        	
        	File fileDir = new File(sPath);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), sEncoding));
    		String line;
        	while ((line = in.readLine()) != null) {
        		// process the line
        		obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                
                String text = (String) jsonObject.get("text");
                String tlText = transLiterateString(text);
                String author = (String) ((JSONObject)jsonObject.get("user")).get("screen_name");
                
                System.out.println("Original Text: " + text);
                System.out.println("TransLiterated Text: " + tlText);
                System.out.println("Author ID: " + author);
                hm.put(author,  "HEB: [" + text + "] TL: [" + tlText + "]");
        	}
        	
        	in.close();	 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void createTransLiterateHM()
	{
		// Hebrew characters
		hmTransLiterate.put("à", "A");
		hmTransLiterate.put("á", "B");
		hmTransLiterate.put("â", "G");
		hmTransLiterate.put("ã", "D");
		hmTransLiterate.put("ä", "H");
		hmTransLiterate.put("å", "W");
		hmTransLiterate.put("æ", "Z");
		hmTransLiterate.put("ç", "X");
		hmTransLiterate.put("è", "J");
		hmTransLiterate.put("é", "I");
		hmTransLiterate.put("ë", "K");
		hmTransLiterate.put("ê", "K");
		hmTransLiterate.put("ì", "L");
		hmTransLiterate.put("î", "M");
		hmTransLiterate.put("í", "M");
		hmTransLiterate.put("ğ", "N");
		hmTransLiterate.put("ï", "N");
		hmTransLiterate.put("ñ", "S");
		hmTransLiterate.put("ò", "E");
		hmTransLiterate.put("ô", "P");
		hmTransLiterate.put("ó", "P");
		hmTransLiterate.put("ö", "C");
		hmTransLiterate.put("õ", "C");
		hmTransLiterate.put("÷", "Q");
		hmTransLiterate.put("ø", "R");
		hmTransLiterate.put("ù", "F");
		hmTransLiterate.put("ú", "T");
		hmTransLiterate.put("\"", "U");
		hmTransLiterate.put(":", "CLN");
		hmTransLiterate.put(";", "SCLN");
		hmTransLiterate.put(".", "DOT");
		hmTransLiterate.put(",", "CM");
		hmTransLiterate.put("-", "DASH");
		hmTransLiterate.put("\"", "QUOT");
		hmTransLiterate.put("?", "QM");
		hmTransLiterate.put("!", "EXCL");
		hmTransLiterate.put("(", "LRB");
		hmTransLiterate.put(")", "RRB");
		hmTransLiterate.put("...", "ELPS");
		hmTransLiterate.put("'", "OPOS");
		
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
		
		//TODO: Handle linefeed. (see "@ArtsiDraw"), &, ,/ ?... all other nulls
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
				if (isSpecialString(sTemp) == true)
					sFormattedString += " " + sTemp + " ";
				else
					sFormattedString += sTemp;
			}
			else 
				sFormattedString += " ";
		}
		
		return sFormattedString;
	}
	
	public static boolean isSpecialString(String s)
	{
		// Returns true is the string (actually a character) is a special case;
		// If it is, the calling method adds a space before it. This is because 
		// e.g. quotes need to be represent separately 
		boolean isSpecial = false;
		
		if (s == null)
			return false;
		
		if (s.equals("CLN") || s.equals("SCLN") || s.equals("DOT") || s.equals("CM") || s.equals("DASH") || s.equals("QUOT") || 
				s.equals("QM") || s.equals("EXCL") || s.equals("LRB") || s.equals("RRB") || s.equals("ELPS") || s.equals("OPOS"))
			isSpecial = true;
		
		return isSpecial;
	}
}
