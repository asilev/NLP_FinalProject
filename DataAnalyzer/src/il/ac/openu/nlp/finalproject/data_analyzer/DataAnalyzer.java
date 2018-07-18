package il.ac.openu.nlp.finalproject.data_analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataAnalyzer {

	private static String sEncoding = "UTF8";
	private static Map<String, Integer> numOfTwittsPerAuthor = new HashMap<>();
	private static List<Integer> histogram ;

	public static void main(String[] args) {
		File dataFile = new File(args[0]);
		Pattern pattern;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), sEncoding ));
			String line;
			pattern = Pattern.compile("^(<[^>]*>) (.*)$");
			int maxNumOfTwitts = 0;
        	while ((line = in.readLine()) != null) {
        		Matcher matcher = pattern.matcher(line);
        		matcher.find();
        		if (numOfTwittsPerAuthor.containsKey(matcher.group(1))) {
        			numOfTwittsPerAuthor.put(matcher.group(1), numOfTwittsPerAuthor.get(matcher.group(1))+1);
        			if (numOfTwittsPerAuthor.get(matcher.group(1)) > maxNumOfTwitts) {
        				maxNumOfTwitts = numOfTwittsPerAuthor.get(matcher.group(1));
        			}
        		}
        		else {
        			numOfTwittsPerAuthor.put(matcher.group(1),1);
        		}
        	}
        	in.close();
        	System.out.println("Num of original authors = "+numOfTwittsPerAuthor.size());
        	histogram = new ArrayList<>();
        	for (int i=0;i<=maxNumOfTwitts;++i) {
        		histogram.add(0);
        	}
        	for (Map.Entry<String, Integer> e : numOfTwittsPerAuthor.entrySet()) {
        		System.out.println(e.getKey()+": "+e.getValue());
        		histogram.set(e.getValue(), histogram.get(e.getValue())+1);
        	}
        	for (int i=1; i<=maxNumOfTwitts;++i) {
        		System.out.println(i+": "+histogram.get(i));
        	}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
