# MedTextClassifier: Knowledge-based Word Sense Disambiguation for classifying clinical text documents
**Languages** - Python, Java, XML/XMI

**Python Libraries** - numpy, pandas, matplotlib, seaborn, sklearn, imblearn, nltk

**Java Libaries** - com.opencsv, org.xml.sax, org.apache.xerces.parsers

**Tools** - Apache cTAKES, Eclipse IDE, Liquid Studio IDE, Google Colab

**Prerequisite** - UMLS license (if using cTAKES).

# Instructions to execute the complete process (cTAKES + JAVA programs + Python script)
1) Download the mtsamples.csv file from the repo location: WSDMedTextClassifier_STRIVERS > Datasets > mtsamples.csv

2) Now, import the Java project 'MedXMIProj' in Eclipse IDE. Open the class MedCsvReader.java. Update the paths for input file 'mtsamples' and output file.  
   Run MedCsvReader.java. A total of 4999 .txt files containing transcription will be generated at the path specified in the program.  

   Note: For reference, go to the repo location: WSDMedTextClassifier_STRIVERS > IntermediateOutputs > AllTxtFiles
   GitHub has a limit of 1000 files per folder.

3) Download Apache cTAKES. Obtain the UMLS license.
   Go to the apache ctakes installation folder and run the default clinical pipeline command:  
   bin/runClinicalPipeline -i <input-directory-name> --xmiOut <output-directory-name> --key <umls-api-key>   
   Here, the input directory will be the directory containing the .txt files (obtained during Step 2) and the output directory will be desired directory to store the xmi        files. The key is the API key associated with your UMLS account.
   This step will take approx. 15-20 min to finish.

   Note: For reference go to the repo location: WSDMedTextClassifier_STRIVERS > IntermediateOutputs > AllXmiFiles
   GitHub has a limit of 1000 files per folder.
  
4) Now, execute the Java program 'UmlsXmiReader.java' after updating the input and output paths as per your system. It will generate a single csv file                            'customMedicalDataset.csv' at the location specified in the program.  

   Note: For reference go to the repo location: WSDMedTextClassifier_STRIVERS > Datasets > customMedicalDataset.csv

5) Now, download the colab notebook 'Final_Strivers_WSD.ipynb' available at the repo location: WSDMedTextClassifier_STRIVERS > Python > Final_Strivers_WSD.ipynb  
   Run it using Google Colab or any other tool such as Jupyter Lab. 

   Note: You need the datasets: mtsamples.csv and customMedicalDataset.csv to execute the python script.

   [GitHub Notebook](https://github.com/sanejait/WSDMedTextClassifier_STRIVERS/blob/main/Python/Final_Strivers_WSD.ipynb)  
   [Colab Notebook - STRIVERS](https://colab.research.google.com/drive/1bDs3ycGc-79hh55RCnQ5z6EA1Ya-pkjX?usp=sharing)

# Instructions for quick execution of program (Python script only - without manual dataset generation)
1) Download the datasets: 'customMedicalDataset.csv' and 'mtsamples.csv' from the repo location: WSDMedTextClassifier_STRIVERS > Datasets

2) Now, download the colab notebook 'Final_Strivers_WSD.ipynb' available at the repo location: WSDMedTextClassifier_STRIVERS > Python > Final_Strivers_WSD.ipynb  
   Run it using Google Colab or any other tool such as Jupyter Lab.

   [GitHub Notebook](https://github.com/sanejait/WSDMedTextClassifier_STRIVERS/blob/main/Python/Final_Strivers_WSD.ipynb)  
   [Colab Notebook - STRIVERS](https://colab.research.google.com/drive/1bDs3ycGc-79hh55RCnQ5z6EA1Ya-pkjX?usp=sharing)
