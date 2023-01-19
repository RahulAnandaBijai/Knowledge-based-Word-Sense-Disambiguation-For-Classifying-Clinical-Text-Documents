import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.SAXParser;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Program to read and parse the XMI file containing UMLS concepts
 * 
 * @author Team Strivers
 * @version 1.0
 */
public class UmlsXmiReader {
	public static void main(String[] args) throws Exception {
		// initialize the array list for csv file data
		List<String[]> medCsvData = new ArrayList<String[]>();
		String[] csvFileHeader = { "transcription", "symptoms", "diseases", "procedures", "medications" };
		medCsvData.add(csvFileHeader);

		// Read all the xmi files from the folder
		// update the file path as per your system
		final File xmiFolder = new File("C:\\AllXmiFiles");
		final List<File> xmiFilesList = Arrays.asList(xmiFolder.listFiles());

		for (File file : xmiFilesList) {
			// create the parser and handler objects
			XMLReader medXmlReader = new SAXParser();
			UmlsXmiHandler xmiHandler = new UmlsXmiHandler();
			// set the content and error handlers
			medXmlReader.setContentHandler(xmiHandler);
			medXmlReader.setErrorHandler(xmiHandler);

			FileReader medFileReader = new FileReader(file);
			// Create Symptoms Sets to avoid duplicates
			// Set to contain the unique symptom names
			Set<String> symptomNamesSet = new HashSet<String>();
			// Set to contain the unique symptom cuids
			Set<String> symptomCuiSet = new HashSet<String>();
			// Set to contain the unique symptom ontology ids
			Set<String> symptomOntologySet = new HashSet<String>();

			// Create Diseases Sets to avoid duplicates
			// Set to contain the unique disease names
			Set<String> diseaseNamesSet = new HashSet<String>();
			// Set to contain the unique disease cuids
			Set<String> diseaseCuiSet = new HashSet<String>();
			// Set to contain the unique disease ontology ids
			Set<String> diseaseOntologySet = new HashSet<String>();

			// Create Procedures Sets to avoid duplicates
			// Set to contain the unique procedure names
			Set<String> procedureNamesSet = new HashSet<String>();
			// Set to contain the unique procedure cuids
			Set<String> procedureCuiSet = new HashSet<String>();
			// Set to contain the unique procedure ontology ids
			Set<String> procedureOntologySet = new HashSet<String>();

			// Create Medications Sets to avoid duplicates
			// Set to contain the unique medication names
			Set<String> medicationNamesSet = new HashSet<String>();
			// Set to contain the unique medication cuids
			Set<String> medicationCuiSet = new HashSet<String>();
			// Set to contain the unique medication ontology ids
			Set<String> medicationOntologySet = new HashSet<String>();

			// Extract the values for Symptoms, Diseases, Procedures, Medications, Umls
			// Concepts and Sofa and put them into Map.
			medXmlReader.parse(new InputSource(medFileReader));
			HashMap<String, String> checkSymptomMap = xmiHandler.getSymptomMap();
			HashMap<String, String> checkDiseaseMap = xmiHandler.getDiseaseMap();
			HashMap<String, String> checkProcedureMap = xmiHandler.getProcedureMap();
			HashMap<String, String> checkMedicationMap = xmiHandler.getMedicationMap();

			HashMap<String, String> checkUmlsMap = xmiHandler.getUmlsConceptsMap();
			HashMap<String, String> sofaMap = xmiHandler.getSofaMap();

			String transcriptionText = "";

			for (Map.Entry<String, String> sympEntry : checkSymptomMap.entrySet()) {
				// Get the Symptom ontology values from Hashmap
				String sympValue = sympEntry.getValue();
				if (sympValue.contains(" ")) {
					// split the values by whitespace
					String[] sympValues = sympValue.split("\\s+");
					for (String str : sympValues) {

						// add all the individual values to set
						symptomOntologySet.add(str);

					}

				} else
					symptomOntologySet.add(sympValue);
			}

			for (Map.Entry<String, String> disEntry : checkDiseaseMap.entrySet()) {
				// insert the disease ontology ids
				diseaseOntologySet.add(disEntry.getValue());
			}

			for (Map.Entry<String, String> procEntry : checkProcedureMap.entrySet()) {
				// insert the procedure ontology ids
				procedureOntologySet.add(procEntry.getValue());
			}

			for (Map.Entry<String, String> medEntry : checkMedicationMap.entrySet()) {
				// insert the medication ontology ids
				medicationOntologySet.add(medEntry.getValue());
			}

			for (Map.Entry<String, String> sofEntry : sofaMap.entrySet()) {
				// set the sofa string - transcription
				transcriptionText = sofEntry.getValue();
			}

			for (Map.Entry<String, String> umlsEntry : checkUmlsMap.entrySet()) {
				/*
				 * Map the UMLS concepts with the corresponding medical concept (Disease,
				 * Symptom, Procedure, Medication) For e.g. Map the UmlsConcept 'id' field with
				 * DiseaseDisorderMention 'ontologyConceptArr' field Then extract the value of
				 * 'preferredText' field.
				 */

				String umlskey = umlsEntry.getKey();
				if (diseaseOntologySet.contains(umlskey)) {
					// split the values by pipe character
					String[] setvals = umlsEntry.getValue().split("\\|");
					diseaseCuiSet.add(setvals[0]);
					diseaseNamesSet.add(setvals[1]);
				} else if (symptomOntologySet.contains(umlskey)) {
					String[] setvals = umlsEntry.getValue().split("\\|");
					// To handle missing values
					if (setvals.length < 2) {

						continue;
					}
					symptomCuiSet.add(setvals[0]);
					symptomNamesSet.add(setvals[1]);
				} else if (procedureOntologySet.contains(umlskey)) {
					// split the values by pipe character
					String[] setvals = umlsEntry.getValue().split("\\|");
					// To handle missing values
					if (setvals.length < 2) {

						continue;
					}
					procedureCuiSet.add(setvals[0]);
					procedureNamesSet.add(setvals[1]);
				} else if (medicationOntologySet.contains(umlskey)) {
					// split the values by pipe character
					String[] setvals = umlsEntry.getValue().split("\\|");
					// To handle missing values
					if (setvals.length < 2) {

						continue;
					}
					medicationCuiSet.add(setvals[0]);
					medicationNamesSet.add(setvals[1]);
				}

			}			

			String[] csvRecord = createMedCsvData(transcriptionText, symptomNamesSet, diseaseNamesSet,
					procedureNamesSet, medicationNamesSet);
			// add the csv record to the Array list
			medCsvData.add(csvRecord);

		}

		// Write all the records to a csv file
		// update the output file path as per your system
		try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Output\\customMedicalDataset.csv"))) {
			// write all the records to the output file 'finalMedDataset'
			System.out.println("Write the complete data to csv file");
			writer.writeAll(medCsvData);
		}

	}

	/**
	 * @param transcription
	 * @param symptoms
	 * @param diseases
	 * @param procedures
	 * @param medications
	 * @return
	 */
	private static String[] createMedCsvData(String transcription, Set<String> symptoms, Set<String> diseases,
			Set<String> procedures, Set<String> medications) {

		// combine all the values of every medical concept, separated by comma
		String symptomsString = String.join(", ", symptoms);
		String diseasesString = String.join(", ", diseases);
		String proceduresString = String.join(", ", procedures);
		String medicationsString = String.join(", ", medications);

		String[] csvRecord = { transcription, symptomsString, diseasesString, proceduresString, medicationsString };

		return csvRecord;
	}

}
