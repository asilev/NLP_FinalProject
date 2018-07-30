package il.ac.openu.nlp.finalproject.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataReader {
	//TODO: Remove this comment
	private File dataFile;
	private BufferedReader dataReader;
	private Pattern pattern = Pattern.compile("^(<[^>]*>) (.*)$");
	private Map<String, BagOfWords> authorBagOfWords = new HashMap<>();
	
	public DataReader(String fileName, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
		dataFile = new File(fileName);
		dataReader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), encoding ));
	}
	
	public Map<String, BagOfWords> setAuthorBagOfWords() throws IOException {
		String line;
		while ((line=dataReader.readLine())!=null) {
			Matcher matcher = pattern.matcher(line);
			matcher.find();
			if (!authorBagOfWords.containsKey(matcher.group(1))) {
				authorBagOfWords.put(matcher.group(1), new BagOfWords());
			}
			String[] words = matcher.group(2).split(" ");
			for (String word : words) {
				authorBagOfWords.get(matcher.group(1)).addWord(word);
			}
		}
		return authorBagOfWords;
	}
}
