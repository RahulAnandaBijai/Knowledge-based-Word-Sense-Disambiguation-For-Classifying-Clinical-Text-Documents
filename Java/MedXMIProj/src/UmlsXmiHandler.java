import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import org.xml.sax.Attributes;

/**
 * Class to handle all elements from the XMI file, extract only the relevant
 * elements and print their attributes.
 * System.out.println statements have been commented intentionally for quicker execution of the program.
 * Uncomment the system.out.println statements for detailed information about elements and debugging
 * 
 * @author Team Strivers
 * @version 1.0
 *
 */
public class UmlsXmiHandler extends DefaultHandler {
	// Map to store disease id and the corresponding ontology ids
	HashMap<String, String> diseaseMap = new HashMap<String, String>();
	// Map to store umls cid and preferred text
	HashMap<String, String> umlsConceptsMap = new HashMap<String, String>();
	// Map to store procedure id and the corresponding ontology ids
	HashMap<String, String> procedureMap = new HashMap<String, String>();
	// Map to store medication id and the corresponding ontology ids
	HashMap<String, String> medicationMap = new HashMap<String, String>();
	// Map to store sofa id and sofa string
	HashMap<String, String> sofaMap = new HashMap<String, String>();
	// Map to store symptom id and the corresponding ontology ids
	HashMap<String, String> symptomMap = new HashMap<String, String>();

	/**
	 * getter method for sofaMap. Returns sofaMap
	 */
	public HashMap<String, String> getSofaMap() {
		return sofaMap;
	}

	/**
	 * setter method for sofaMap
	 */
	public void setSofaMap(HashMap<String, String> sofaMap) {
		this.sofaMap = sofaMap;
	}

	/**
	 * getter method for symptomMap. Returns symptomsMap
	 */
	public HashMap<String, String> getSymptomMap() {
		return symptomMap;
	}

	/**
	 * setter method for symtomMap
	 */
	public void setSymptomMap(HashMap<String, String> symptomMap) {
		this.symptomMap = symptomMap;
	}

	/**
	 * getter method for medicationMap. Returns medicationMap
	 */
	public HashMap<String, String> getMedicationMap() {
		return medicationMap;
	}

	/**
	 * setter method for medicationMap
	 */
	public void setMedicationMap(HashMap<String, String> medicationMap) {
		this.medicationMap = medicationMap;
	}

	/**
	 * getter method for diseaseMap. Returns the diseaseMap
	 */
	public HashMap<String, String> getDiseaseMap() {
		return diseaseMap;
	}

	/**
	 * setter method for diseaseMap
	 */
	public void setDiseaseMap(HashMap<String, String> diseaseMap) {
		this.diseaseMap = diseaseMap;
	}

	/**
	 * getter method for umlsConceptsMap. Returns the umlsConceptsMap
	 */
	public HashMap<String, String> getUmlsConceptsMap() {
		return umlsConceptsMap;
	}

	/**
	 * setter method for umlsConceptsMap
	 */
	public void setUmlsConceptsMap(HashMap<String, String> umlsConceptsMap) {
		this.umlsConceptsMap = umlsConceptsMap;
	}

	/**
	 * getter method for umlsConceptsMap. Returns the procedureMap
	 */
	public HashMap<String, String> getProcedureMap() {
		return procedureMap;
	}

	/**
	 * setter method for procedureMap
	 */
	public void setProcedureMap(HashMap<String, String> procedureMap) {
		this.procedureMap = procedureMap;
	}

	// To tell about the start of xmi file
	// uncomment the system.out.println statements for detailed information/debugging
	public void startDocument() {		
		//System.out.println("Begin: XMI file. ");
	}

	// To tell about the end of xmi file
	// uncomment the system.out.println statements for detailed information/debugging
	public void endDocument() {		
		//System.out.println("End: XMI file ");
	}

	// Print the element name and the corresponding attributes
	// Uncomment the System.out.println statement for detailed information/debugging
	public void startElement(String elementUri, String name, String qualName, Attributes attr) {
		// Run this process only for required fields such as Procedures, Medications,
		// Symptoms, Diseases, Sofa and UMLS Concepts

		if (name.equals("SignSymptomMention") || name.equals("ProcedureMention") || name.equals("MedicationMention")
				|| name.equals("DiseaseDisorderMention") || name.equals("UmlsConcept") || name.equals("Sofa")) {
			if (name.equals("SignSymptomMention")) {
				symptomMap.put(attr.getValue(0), attr.getValue(5));

			} else if (name.equals("DiseaseDisorderMention")) {
				diseaseMap.put(attr.getValue(0), attr.getValue(5));

			} else if (name.equals("Sofa")) {
				sofaMap.put(attr.getValue(0), attr.getValue(4));

			} else if (name.equals("ProcedureMention")) {
				procedureMap.put(attr.getValue(0), attr.getValue(5));

			} else if (name.equals("MedicationMention")) {
				medicationMap.put(attr.getValue(0), attr.getValue(5));

			} else if (name.equals("UmlsConcept")) {
				String cid = "";
				String preferredText = "";

				if (attr.getLength() > 0) {

					for (int index = 0; index < attr.getLength(); ++index) {

						if (attr.getLocalName(index).equals("cui"))

							cid = attr.getValue(index);

						if (attr.getLocalName(index).equals("preferredText"))

							preferredText = attr.getValue(index);
					}
				}

				String id = attr.getValue(0);

				if (!id.isEmpty() && !cid.isEmpty() && !preferredText.isEmpty()) {
					umlsConceptsMap.put(id, cid.concat("|").concat(preferredText));
				}
			}
			
			//System.out.println("start element: " + name);
			// check that attribute is not empty
			if (attr.getLength() > 0) {
				//uncomment the System.out.println statement for detailed information/debugging
				//System.out.println(" attributes:");
				@SuppressWarnings("unused")
				String attributes = " ";
				for (int index = 0; index < attr.getLength(); ++index)
					// concatenate the id and value separated by colon
					attributes += attr.getLocalName(index) + ": '" + attr.getValue(index) + "' ";				
				//System.out.println(attributes);
				//System.out.println("finish");
			}

			else {				
				//System.out.println("no attributes");
			}
		}
	}

	// To tell about end of Element
	// Uncomment the System.out.println statements for detailed information/debugging
	public void endElement(String elementUri, String name, String qualName) {
		// Run this process only for required fields such as Procedures, Medications,
		// Symptoms, Diseases, Sofa and UMLS Concepts

		if (name.equals("SignSymptomMention") || name.equals("ProcedureMention") || name.equals("MedicationMention")
				|| name.equals("DiseaseDisorderMention") || name.equals("UmlsConcept") || name.equals("Sofa"))
		{			
			//System.out.println("end Element: " + name);
		}
	}

	// Print all the characters and remove the whitespaces
	// Uncomment the System.out.println statements for detailed information/debugging
	public void characters(char ch[], int begin, int len) {
		@SuppressWarnings("unused")
		String characters = new String(ch, begin, len);		
		//System.out.println("Print Characters: '" + characters.trim() + "'");
	}
}