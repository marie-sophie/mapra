package main;

import java.util.HashMap;
import java.util.LinkedList;

import io.FileOutputWriter;
import phenomizer.algorithm.DataTransformer;
import phenomizer.algorithm.FrequencyConverter;
import phenomizer.algorithm.Ontology;
import phenomizer.algorithm.SymptomDiseaseAssociations;
import phenomizer.io.FileUtilitiesPhenomizer;

public class GetPhenoDisStats {
	
	/**
	 * main method for running the calculation of PhenoDis statistics (symptoms per diseases)
	 * @param args command line parameters
	 * 	position 0: path to symptom table of PhenoDis, position 1: path to ontology table of PhenoDis,
	 * 	position 2: path to ksz table of PhenoDis, position 3: path to write disease statistics
	 * 	position 4: path to write symptom statistics
	 */
	public static void main(String args[]){
		
		String symptomsFile = args[0];
		String ontoFile = args[1];
		String kszFile = args[2];
		String out_disease = args[3];
		String out_symptom = args[4];
		
		LinkedList<Integer> symptomsRaw = FileUtilitiesPhenomizer.readInSymptoms(symptomsFile);
		int [][] ontoRaw = FileUtilitiesPhenomizer.readInOntology(ontoFile);
		HashMap<Integer, LinkedList<Integer[]>> kszRaw = new FrequencyConverter().convertAll(
				FileUtilitiesPhenomizer.readInKSZFrequency(kszFile)); 
		
		Ontology o = new Ontology(ontoRaw);
		SymptomDiseaseAssociations sda = new DataTransformer().generateSymptomDiseaseAssociation(o, symptomsRaw, kszRaw);
		
		FileOutputWriter fow = new FileOutputWriter(out_disease);
		fow.writeFileln("DiseaseId\tNumberOfSymptoms");
		for(int id: sda.getDiseases()){
			LinkedList<Integer[]> symptomsDis = sda.getSymptoms(id);
			fow.writeFileln(id+"\t"+symptomsDis.size());
		}
		fow.closew();
		
		fow = new FileOutputWriter(out_symptom);
		fow.writeFileln("SymptomId\tNumberOfDiseases");
		for(int id: sda.getSymptoms()){
			fow.writeFileln(id+"\t"+sda.numberOfDiseases(id));
		}
		fow.closew();
		
	}

}
