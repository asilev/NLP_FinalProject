package il.ac.openu.nlp.finalproject.cmb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConfusionMatrixBuilder {
	public static void main(String[] args) throws IOException {
		String taggedFilename = args[0];
		String evaluatedFilename = args[1];
//		String taggedFilename = "C:\\NLP\\ConfusionMatrix\\evaluation.svm.fc.NumOfCharacters";
//		String evaluatedFilename = "C:\\NLP\\ConfusionMatrix\\eval.NumOfCharacters";

		double[][] numOfHits = new double[10][10];
		int[] totalInstances = new int[10];
		BufferedReader tagged = new BufferedReader(new InputStreamReader(new FileInputStream(new File(taggedFilename)), "UTF8"));
		BufferedReader evaluated = new BufferedReader(new InputStreamReader(new FileInputStream(new File(evaluatedFilename)), "UTF8"));
		String taggedStr;
		String evalStr;
        while ((taggedStr = tagged.readLine()) != null) {
        	evalStr = evaluated.readLine();
        	String tag = taggedStr.split(" ")[0];
        	numOfHits[Integer.parseInt(tag)][Integer.parseInt(evalStr)]++;
        	totalInstances[Integer.parseInt(tag)]++;        	
        }
        for (int i=0;i<10;++i) {
        	for (int j=0;j<10;++j) {
        		numOfHits[i][j] /= totalInstances[i];
        	}
        }
        tagged.close();
        evaluated.close();
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("confusion.csv")),"UTF8"));
        for (int i=0;i<10;++i) {
        	for (int j=0;j<10;++j) {
        		output.write(numOfHits[i][j]+",");
        	}
        	output.write("\n");
        }
        output.close();
	}
}
