package il.ac.openu.nlp.finalproject.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TrainingDataWriter {
	
	private File trainingDataFile;
	private FileOutputStream trainingDataFileOutputStream;
	private ObjectOutputStream trainingDataObjectOutputStream;
	
	public TrainingDataWriter(String filename) throws IOException {
		trainingDataFile = new File(filename);
		trainingDataFileOutputStream = new FileOutputStream(trainingDataFile);
		trainingDataObjectOutputStream = new ObjectOutputStream(trainingDataFileOutputStream);
	}
	
	public void write(TrainingDataRecord trainingDataRecord) throws IOException {
		trainingDataObjectOutputStream.writeObject(trainingDataRecord);
	}
	
	public void close() throws IOException {
		trainingDataFileOutputStream.close();
	}
}
