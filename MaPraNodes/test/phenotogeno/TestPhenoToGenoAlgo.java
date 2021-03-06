package phenotogeno;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Test;

import phenotogeno.algo.PhenoToGenoDataTransformer;
import phenotogeno.io.FileUtilitiesPTG;
import togeno.AnnotatedGene;
import togeno.AnnotatedGeneMax;
import togeno.AnnotatedGeneMultiple;
import togeno.GeneAssociation;
import togeno.ScoredDiseaseOrMetabolite;
import togeno.ScoredGene;
import togeno.ScoredGeneComparator;
import togeno.ToGenoAlgo;

public class TestPhenoToGenoAlgo {
	
	private GeneAssociation dga;
	
	private void generateDGA(boolean multiple){
		LinkedList<String> genes_raw =
				FileUtilitiesPTG.readGeneList("../TestData/PhenoToGeno/all_genes.txt");
		HashMap<Integer, LinkedList<String>> mapping =
				FileUtilitiesPTG.readDiseaseGeneAssociation("../TestData/PhenoToGeno/gene_diseases.txt");
		PhenoToGenoDataTransformer dt = new PhenoToGenoDataTransformer();
		dga =dt.getDiseaseGeneAssociation(genes_raw, mapping, multiple);
	}
	
	@Test
	public void testAnnotationOfGenesMultiple() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		generateDGA(true);
		runAnnotateGenes();
		checkAnnotation(true);
	}
	
	@Test
	public void testAnnotationOfGenesMax() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		generateDGA(false);
		runAnnotateGenes();
		checkAnnotation(false);
	}
	
	@Test
	public void testGenerationOfResultMultiple() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		generateDGA(true);
		//annotated genes to inject instead of previously generated dga
		AnnotatedGene g1= new AnnotatedGeneMultiple("G1");
		AnnotatedGene g2= new AnnotatedGeneMultiple("G2");
		g2.add("100", 0.1);
		AnnotatedGene g3= new AnnotatedGeneMultiple("G3");
		g3.add("101", 1.0);
		g3.add("102", 0.5);
		AnnotatedGene g4= new AnnotatedGeneMultiple("G4");
		g4.add("103", 0.05);
		g4.add("104", 0.05);
		AnnotatedGene g5= new AnnotatedGeneMultiple("G5");
		g5.add("105", 0.1);
		g5.add("106", 0.1);
		g5.add("110", 0.01);
		g5.add("107", 0.1);
		g5.add("109", 0.01);
		g5.add("108", 0.1);
		AnnotatedGene g6= new AnnotatedGeneMultiple("G6");
		g6.add("111", 0.05);
		g6.add("112", 0.05);
		AnnotatedGene[] genes = new AnnotatedGene[]{g1,g2,g3,g4,g5,g6};
		
		//expected results
		double[] expected_scores = new double[]{0,0.1, 1, 0.0975, 0.35695639, 0.0975};
		String[] expected_origin = new String[]{"", "100", "101", "103,104", "105,106,107...", "111,112"};
		String[] sorted_ids = new String []{"G3", "G5", "G2", "G4", "G6", "G1"};
		double[] sorted_scores = new double[] {1, 0.35695639, 0.1, 0.0975, 0.0975, 0.0};
		
		checkToGenoResult(genes, expected_scores, expected_origin, sorted_ids, sorted_scores);
	}
	
	@Test
	public void testGenerationOfResultMax() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		generateDGA(false);
		//annotated genes to inject instead of previously generated dga
		AnnotatedGene g1= new AnnotatedGeneMax("G1");
		AnnotatedGene g2= new AnnotatedGeneMax("G2");
		g2.add("100", 0.1);
		AnnotatedGene g3= new AnnotatedGeneMax("G3");
		g3.add("101", 1.0);
		g3.add("102", 0.5);
		AnnotatedGene g4= new AnnotatedGeneMax("G4");
		g4.add("103", 0.05);
		g4.add("104", 0.05);
		AnnotatedGene g5= new AnnotatedGeneMax("G5");
		g5.add("105", 0.1);
		g5.add("106", 0.1);
		g5.add("110", 0.01);
		g5.add("107", 0.1);
		g5.add("109", 0.01);
		g5.add("108", 0.1);
		AnnotatedGene g6= new AnnotatedGeneMax("G6");
		g6.add("111", 0.05);
		g6.add("112", 0.05);
		AnnotatedGene[] genes = new AnnotatedGene[]{g1,g2,g3,g4,g5,g6};
		
		//expected results
		double[] expected_scores = new double[]{0.0, 0.1, 1, 0.05, 0.1, 0.05};
		String[] expected_origin = new String[]{"", "100", "101", "103,104", "105,106,107...", "111,112"};
		double[] sorted_scores = new double[] {1, 0.1, 0.1, 0.05, 0.05, 0.0};
		String[] sorted_ids = new String []{"G3", "G2", "G5", "G4", "G6", "G1"};
		
		checkToGenoResult(genes, expected_scores, expected_origin, sorted_ids, sorted_scores);
	}
	
	@SuppressWarnings("all")
	private void runAnnotateGenes() throws NoSuchMethodException, SecurityException, 
			IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		LinkedList<ScoredDiseaseOrMetabolite> pred = new LinkedList<ScoredDiseaseOrMetabolite>();
		pred.add(new ScoredDiseaseOrMetabolite(100, 0.0));
		pred.add(new ScoredDiseaseOrMetabolite(101, 1.0));
		ToGenoAlgo algo = new ToGenoAlgo(pred, dga);
		
		Method m = ToGenoAlgo.class.getDeclaredMethod("annotateGenes",null);
		m.setAccessible(true);
		m.invoke(algo, null);
	}
	
	private void checkAnnotation(boolean multiple){
		
		AnnotatedGene[] genes = dga.getAllGenes();
		HashSet<String> anno = new HashSet<String>();
		for(String s : new String[] {"MTG1", "MTG2", "MTG3", "MTG4", "MTG14"}){
			anno.add(s);
		}
		
		double genesRemainder = 1d/50;
		double genesof101 = -1;
		if(multiple){
			genesof101 = 1-(1-1d/50)*(1-1d/12); 
		}
		else{
			genesof101 = 1d/12;
		}
		
		for (AnnotatedGene g: genes){
			if(anno.contains(g.getId())){
				assertArrayEquals("Disease annotation for "+g.getId()+" is incorrect"
						,new String[]{"101"} ,g.getContributorIds());
				assertEquals("Probability for "+g.getId()+" is incorrect",
						genesof101, g.getFinalScore(), 1E-10);
			}
			else{
				assertArrayEquals("Disease annotation for "+g.getId()+" is incorrect"
						,new String[]{"100"} ,g.getContributorIds());
				assertEquals("Probability for "+g.getId()+" is incorrect",
						genesRemainder, g.getFinalScore(), 1E-10);
			}
		}
	}
	
	private void checkToGenoResult(AnnotatedGene[] genes, double [] expected_scores, String[] expected_origin,
			String[] sorted_ids, double [] sorted_scores) 
					throws NoSuchMethodException, SecurityException, IllegalAccessException, 
					IllegalArgumentException, InvocationTargetException{
		
		ToGenoAlgo algo = new ToGenoAlgo(null, dga);
		Method m = ToGenoAlgo.class.getDeclaredMethod("scoreGenes",AnnotatedGene[].class);
		m.setAccessible(true);
		@SuppressWarnings("unchecked")
		LinkedList<ScoredGene> res =(LinkedList<ScoredGene>) m.invoke(algo, (Object) genes);
		
		for(int i=0;i<6; i++){
			assertEquals("Score for gene "+(i+1)+" is incorrect",
					expected_scores[i], res.get(i).getScore(), 1E-5);
			assertEquals("Annotation for gene "+(i+1)+" is incorrect",
					expected_origin[i], res.get(i).getImportantContributors());
		}
		
		//check sorting function
		Collections.sort(res, new ScoredGeneComparator());
		for(int i=0;i<6; i++){
			assertEquals("Id for position "+i+" is incorrect",
					sorted_ids[i], res.get(i).getId());
			assertEquals("Score for gene "+i+" is incorrect",
					sorted_scores[i], res.get(i).getScore(), 1E-5);
		}
	}

}
