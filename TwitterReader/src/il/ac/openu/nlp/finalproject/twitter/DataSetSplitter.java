package il.ac.openu.nlp.finalproject.twitter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class DataSetSplitter {
	// This class is a utility that takes the YAP output files (.conll) and splits them into 
	// training and golden files, using a parameter that tells the ratio to split (e.g. 80%)
	
	private static double splitRatio = 0.8;		// A default value, otherwise given from args
	private static String sWorkDir = "";
	private static File[] listOfFiles = null;

	public DataSetSplitter()
	{
		File folder = new File(sWorkDir);
		listOfFiles = folder.listFiles();
	}
	
	public void splitDataSetFiles()
	{
		List<String> lines;
		int numOfLines = 0;
		int linesCounter = 0;

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println("Processing the file: " + file.getName());
		        String fullFileName = file.getAbsolutePath();
		        String fileWithoutExt = fullFileName.substring(0, fullFileName.indexOf("."));
		        String trainFileName = fileWithoutExt + ".test";
		        String goldenFileName = fileWithoutExt + ".gold";
		        
		        try {
					lines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);
					numOfLines = lines.size();
					String line = "";
					
					PrintWriter outTrain = new PrintWriter(new File(trainFileName), "UTF-8");
					PrintWriter outGold = new PrintWriter(new File(goldenFileName), "UTF-8");
					
					System.out.println("Creating the TRAIN file \"" + trainFileName + "\"");
					System.out.println("Creating the GOLD file \"" + goldenFileName + "\"");

					// Now split the whole data set into two files
					while (linesCounter < numOfLines * splitRatio)
					{
						line = lines.get(linesCounter++);
						outTrain.write(line + "\n");
					}
					
					// At this point we may be in the middle of a tweet, so finish
					// it first before writing the remainder of the file to 
					// the second file (the golden one)
					while (lines.get(linesCounter).equals("") == false)
					{
						line = lines.get(linesCounter++);
						outTrain.write(line + "\n");
					}
					
					// Now write the output to the golden file
					while (linesCounter < numOfLines)
					{
						line = lines.get(linesCounter++);
						outGold.write(line + "\n");
					}
					
					outTrain.close();
					outGold.close();
					lines.clear();
					linesCounter = 0;
					numOfLines = 0;
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
	}

	public static void main(String[] args) {

		if (args.length > 0)
		{
			sWorkDir = args[0];
			System.out.println("Working directory is: " + sWorkDir);
			splitRatio = Double.parseDouble(args[1])/100;
			System.out.println("Split ratio is: " + splitRatio);
		}

		DataSetSplitter dsp = new DataSetSplitter();
		dsp.splitDataSetFiles();
	}
}
