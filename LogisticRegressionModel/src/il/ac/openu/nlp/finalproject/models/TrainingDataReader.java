package il.ac.openu.nlp.finalproject.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class TrainingDataReader implements Iterable<TrainingDataRecord>{
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
	public Iterator<TrainingDataRecord> iterator() {
		try {
			return new Iterator<TrainingDataRecord>() {
				private TrainingDataRecord nextDataRecord = (TrainingDataRecord) trainingDataObjectInputStream.readObject();
				private boolean hasNext = nextDataRecord!=null;
				
				@Override
				public boolean hasNext() {
					return hasNext;
				}

				@Override
				public TrainingDataRecord next() {
					TrainingDataRecord thisDataRecord = nextDataRecord; 
					try {
						nextDataRecord = (TrainingDataRecord) trainingDataObjectInputStream.readObject();
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
