package il.ac.openu.nlp.finalproject.models.bagofwords;

import java.io.IOException;
import java.util.List;

import il.ac.openu.nlp.finalproject.models.StructuredDataReader;
import il.ac.openu.nlp.finalproject.models.TaggedFeatureVector;

public class BagOfWordsModel {
	private static StructuredDataReader dataReader;
	private static List<TaggedFeatureVector<String>> taggedFeatureVectorList;
	
	public static void main(String[] args) throws IOException {
		if (args.length>0) {
			if (args.length==1) {
				dataReader = new StructuredDataReader(args[0], "UTF8");
			}
			else if (args.length > 1) {
				dataReader = new StructuredDataReader(args[0], args[1]);
			}
		}
		else {
			printUsage();
			System.exit(-1);
		}
		taggedFeatureVectorList = dataReader.buildAuthorBagOfWords();
	}

	private static void printUsage() {
		System.out.println("Usage: BagOfWordsModel <output from yac folder> <encoding(=UTF8)>");
	}
}