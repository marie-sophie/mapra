package main;

import io.FileUtilities;
import phenomizeralgorithm.FrequencyConverter;
import phenomizeralgorithm.ScoreDistributionSampling;

import java.util.HashMap;
import java.util.LinkedList;

public class CalcPValue {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		int queryLength = Integer.valueOf(args[0]);
		int iterations = Integer.valueOf(args[1]);
		// noweight, weight, asymmweight
		String weight =args[2]; 
		String ontoIn = args[3];
		String kszIn = args[4];
		String symptomsIn = args[5];
		String output = args[6];
		String progress = args[7];
		
		int weighting=-1;
		if(weight.equals("noweight")){
			System.out.println("not weighted");
			weighting=0;
		}
		else if(weight.equals("weight")){
			System.out.println("symmetric weight");
			weighting=2;
		}
		else if(weight.equals("asymmweight")){
			System.out.println("asymmetric weight");
			weighting=1;
		}
		else{
			throw new Exception("Incorrect argument at pos 2!");
		}
		
		//read in files
		int[][]ontology = FileUtilities.readInOntology(ontoIn);
		LinkedList<Integer> symptoms = FileUtilities.readInSymptoms(symptomsIn);
		HashMap<Integer, LinkedList<Integer[]>> ksz = new HashMap<Integer, LinkedList<Integer[]>>();
		
		//prepare data
		FrequencyConverter f = new FrequencyConverter();
		if(weighting==0){
			HashMap<Integer,LinkedList<Integer>>kszTmp = FileUtilities.readInKSZ(kszIn);
			ksz= f.addWeights(kszTmp);
		}
		else{
			HashMap<Integer,LinkedList<String[]>>kszTmp = FileUtilities.readInKSZFrequency(kszIn);
			ksz=f.convertAll(kszTmp);
		}
		
		ScoreDistributionSampling sds = new ScoreDistributionSampling(
				queryLength, iterations, weighting, ontology, ksz, symptoms, progress, output);
		sds.prepareData();
		sds.startSampling();
		
	}
	
}
