package il.ac.openu.nlp.finalproject.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class TrainingDataReader implements Iterable<ClassifiedFeatureRecord<String>>{
	private File trainingDataFile;
	private FileInputStream trainingDataFileInputStream;
	private ObjectInputStream trainingDataObjectInputStream;

	public TrainingDataReader(String filename) throws IOException {
		trainingDataFile = new File(filename);
		trainingDataFileInputStream = new FileInputStream(trainingDataFile);
		trainingDataObjectInputStream = new ObjectInputStream(trainingDataFileInputStream);
	}
	
	public void close() throws IOException {
		trainingDataFileInputStream.close();
	}
	
	@Override
	public Iterator<ClassifiedFeatureRecord<String>> iterator() {
		try {
			return new Iterator<ClassifiedFeatureRecord<String>>() {
				private ClassifiedFeatureRecord<String> nextDataRecord = getNextRecord();

				@SuppressWarnings("unchecked")
				private ClassifiedFeatureRecord<String> getNextRecord() throws IOException, ClassNotFoundException {
					Object o = trainingDataObjectInputStream.readObject();
					if (o instanceof ClassifiedFeatureRecord<?>) {
						return (ClassifiedFeatureRecord<String>)o ;
					}
					return null;
				}
				private boolean hasNext = nextDataRecord!=null;
				
				@Override
				public boolean hasNext() {
					return hasNext;
				}

				@Override
				public ClassifiedFeatureRecord<String> next() {
					ClassifiedFeatureRecord<String> thisDataRecord = nextDataRecord; 
					try {
						nextDataRecord = getNextRecord();
					} catch (ClassNotFoundException | IOException e) {
						hasNext=false;
					}
					return thisDataRecord;
				}
			};
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
}
