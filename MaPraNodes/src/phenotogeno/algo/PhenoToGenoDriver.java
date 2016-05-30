package phenotogeno.algo;

import java.util.HashMap;
import java.util.LinkedList;

import togeno.GeneAssociation;
import togeno.ScoredDiseaseOrMetabolite;
import togeno.ScoredGene;
import togeno.ToGenoAlgo;

//call this class from node to run PhenoToGeno
public class PhenoToGenoDriver {
	
	private LinkedList<String[]> phenomizer_raw;
	private LinkedList<String> genes_raw;
	private HashMap<Integer, LinkedList<String>> mapping;
	
	private LinkedList<ScoredDiseaseOrMetabolite> phenomizer;
	private GeneAssociation dga;
	
	/**
	 * generates a driver for PhenoToGeno,
	 * 		this objects handles the data needed for PhenoToGeno and runs the PhenoToGenoAlgorithm
	 * @param phenomizer_raw list of String arrays, each array represents one disease with results from Phenomizer
	 * 			array[0]: PhenoDis disease id , array[1]: pvalue
	 * @param genes_raw list of gene ids as Strings (e.g. ensembl ids),
	 * 			assumption: each id occurs exactly once
	 * @param mapping map disease id-> list of gene ids
	 */
	public PhenoToGenoDriver(LinkedList<String[]> phenomizer_raw, LinkedList<String> genes_raw,
			HashMap<Integer, LinkedList<String>> mapping){
		this.phenomizer_raw = phenomizer_raw;
		this.genes_raw = genes_raw;
		this.mapping = mapping;
	}
	
	/**
	 * method to execute the PhenoToGeno algorithm
	 * @return list of ScoredGenes
	 */
	public LinkedList<ScoredGene> runPhenoToGeno(){
		prepareData();
		ToGenoAlgo ptg = new ToGenoAlgo(phenomizer, dga);
		return ptg.runToGene();
	}
	
	/**
	 * method to handle the input data for PhenoToGenoAlgorithm
	 */
	private void prepareData(){
		
		PhenoToGenoDataTransformer dt = new PhenoToGenoDataTransformer();
		// association disease - gene
		dga = dt.getDiseaseGeneAssociation(genes_raw, mapping);
		// list of scored diseases
		phenomizer = dt.getPhenomizerResult(phenomizer_raw, dga);
	}

}
