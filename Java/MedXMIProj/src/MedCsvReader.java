import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MedCsvReader {

    public static void main(String[] args) throws IOException, CsvException {
    	// update the mtsamples file path as per your system
        String fileName = "C:\\Users\\aneja\\My Drive\\St. Francis\\STFX_2021\\Lectures\\Bioinformatics\\Project\\mtsamples.csv";
        try (CSVReader fReader = new CSVReader(new FileReader(fileName))) {
            String[] rowContent;
            int count = 1;
            while ((rowContent = fReader.readNext()) != null) {
            	if (rowContent[1].equals("description") &&  rowContent[2].equals("medical_specialty")) {
            		System.out.println("Ignore Header");
            		continue;
            	}
            	String transcription = rowContent[4];
            	
            	// Write only the transcription to the output .txt file
            	try {
            		// update the output file path as per your system
            		String outputFileName = "C:\\AllTxtFiles\\"+"trans"+count+".txt";
            		  File oFile = new File(outputFileName);
            		
            	      FileWriter fWriter = new FileWriter(oFile);
            	      fWriter.write(transcription);
            	      fWriter.close();
            	      System.out.println("Transcription written to file");
            	    } catch (IOException exception) {
            	      System.out.println("Error while writing transcription:" + transcription);
            	      exception.printStackTrace();
            	    }
            	
            	count++;
            }
        }

    }

}