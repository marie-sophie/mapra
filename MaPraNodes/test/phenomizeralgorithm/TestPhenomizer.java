package phenomizeralgorithm;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import io.FileInputReader;
import io.FileUtilities;
import main.CalcPValue;

public class TestPhenomizer {
	
	private LinkedList<Integer> query;
	private LinkedList<Integer> symptoms;
	private int [][] ontology;
	private HashMap<Integer,LinkedList<Integer[]>> ksz_no_freq;
	private HashMap<Integer,LinkedList<Integer[]>> ksz_with_freq;
	private LinkedList<String> expected_res;
	
	private void readData(int i, boolean weight, boolean pval){
		query = FileUtilities.readInQuery("../TestData/Queries/query"+i+".txt");
		symptoms = FileUtilities.readInSymptoms("../TestData/DiseasesAndSymptoms/symptoms.txt");
		ontology = FileUtilities.readInOntology("../TestData/DiseasesAndSymptoms/Ontology.txt");
		
		ksz_no_freq= CalcPValue.addWeights(
				FileUtilities.readInKSZ("../TestData/DiseasesAndSymptoms/ksz.txt"));
		ksz_with_freq = FrequencyConverter.convertAll(
				FileUtilities.readInKSZFrequency("../TestData/DiseasesAndSymptoms/ksz_freq.txt"));	
		
		if(!weight){
			if(!pval){
				expected_res = FileInputReader.readAllLinesFrom("../TestData/ExpectedResults/NoWeightNoP/res_q"+i+".txt");
			}
			else{
				expected_res = FileInputReader.readAllLinesFrom("../TestData/ExpectedResults/NoWeightP/res_q"+i+".txt");
				PValueFolder.setPvalFoder("../TestData/PValues");
			}
		}
		else{
			expected_res = FileInputReader.readAllLinesFrom("../TestData/ExpectedResults/WeightNoP/res_q"+i+".txt");
		}
		//remove table header
		expected_res.remove(0);
	}

	@Test
	public void testPhenomizer_NoWeight_NoPVal() {
		
		for(int i=1; i<=10; i++){
			readData(i,false, false);
			AlgoPheno.setInput(query, symptoms, ksz_no_freq, ontology);
			LinkedList<String[]> result = AlgoPheno.runPhenomizer(11,false);
			for(int j=0; j<=10; j++){
				String[] elements = expected_res.get(j).split("\t");
				assertEquals("Query "+i+" result "+j+" does not match expected disease id",
						elements[0], result.get(j)[0]);
				assertEquals("Query "+i+" result "+j+" does not match expected score",
						elements[2], result.get(j)[1]);
			}
		}
	}
	
	@Test
	public void testPhenomizer_Weight_NoPVal() {
		
		for(int i=1; i<=10; i++){
			readData(i,true, false);
			AlgoPheno.setInput(query, symptoms, ksz_with_freq, ontology);
			LinkedList<String[]> result = AlgoPheno.runPhenomizer(11,false);
			for(int j=0; j<=10; j++){
				String[] elements = expected_res.get(j).split("\t");
				assertEquals("Query "+i+" result "+j+" does not match expected disease id",
						elements[0], result.get(j)[0]);
				assertEquals("Query "+i+" result "+j+" does not match expected score",
						elements[2], result.get(j)[1]);
			}
		}
	}
	
	@Test
	public void testPhenomizer_NoWeight_PVal() {
		
		for(int i=1; i<=10; i++){
			readData(i,false, true);
			LinkedList<String[]> result = PValueGenerator.phenomizerWithPValues(
					11, query, symptoms, ksz_no_freq, ontology, false);
			for(int j=0; j<=10; j++){
				String[] elements = expected_res.get(j).split("\t");
				assertEquals("Query "+i+" result "+j+" does not match expected disease id",
						elements[0], result.get(j)[0]);
				assertEquals("Query "+i+" result "+j+" does not match expected score",
						elements[2], result.get(j)[1]);
				assertEquals("Query "+i+" result "+j+" dose not match expected pvalue", 
						elements[3], result.get(j)[2]);				
			}
		}	
	}
	
	@Test
	public void testPhenomizer_LimitedOutput(){
		
		//limited to 4, but output 5 because last 2 elements have the same score
		readData(5, false, false);
		expected_res=FileInputReader.readAllLinesFrom(
				"../TestData/ExpectedResults/ResultSizeLimited/res_q5_noweight_nopval.txt");
		expected_res.remove(0);
		AlgoPheno.setInput(query, symptoms, ksz_no_freq, ontology);
		LinkedList<String[]> result = AlgoPheno.runPhenomizer(4,false);
		assertEquals("Output size (query 5 imited to 4, no weight, no pvalue) does not match expected output",
				5, result.size());
		for(int j=0; j<=4; j++){
			String[] elements = expected_res.get(j).split("\t");
			assertEquals("Query 5 (limited to 4, no weight, no pvalue) result "+j+" does not match expected disease id",
					elements[0], result.get(j)[0]);
			assertEquals("Query 5 (limited to 4, no weight, no pvalue) result "+j+" does not match expected score",
					elements[2], result.get(j)[1]);
		}
		
		//limited to 4, expected 4 elements
		readData(5, true, false);
		expected_res=FileInputReader.readAllLinesFrom(
				"../TestData/ExpectedResults/ResultSizeLimited/res_q5_weight_nopval.txt");
		expected_res.remove(0);
		AlgoPheno.setInput(query, symptoms, ksz_with_freq, ontology);
		result = AlgoPheno.runPhenomizer(4,false);
		assertEquals("Output size (query 5 imited to 4, weight, no pvalue) does not match expected output",
				4, result.size());
		for(int j=0; j<=3; j++){
			String[] elements = expected_res.get(j).split("\t");
			assertEquals("Query 5 (limited to 4, weight, no pvalue) result "+j+" does not match expected disease id",
					elements[0], result.get(j)[0]);
			assertEquals("Query 5 (limited to 4, weight, no pvalue) result "+j+" does not match expected score",
					elements[2], result.get(j)[1]);
		}
		
		//limited to 4, expected 4 elements, does not consider if last elements have same pvalue
		readData(5, false, true);
		expected_res=FileInputReader.readAllLinesFrom(
				"../TestData/ExpectedResults/ResultSizeLimited/res_q5_noweight_pval.txt");
		expected_res.remove(0);
		result = PValueGenerator.phenomizerWithPValues(
				4, query, symptoms, ksz_no_freq, ontology, false);
		assertEquals("Output size (query 5 imited to 4, no weight, pvalue) does not match expected output",
				4, result.size());
		for(int j=0; j<=3; j++){
			String[] elements = expected_res.get(j).split("\t");
			assertEquals("Query 5 (limited to 4, no weight, pvalue) result "+j+" does not match expected disease id",
					elements[0], result.get(j)[0]);
			assertEquals("Query 5 (limited to 4, no weight, pvalue) result "+j+" does not match expected score",
					elements[2], result.get(j)[1]);
			assertEquals("Query 5 (limited to 4, no weight, pvalue) result "+j+" dose not match expected pvalue", 
					elements[3], result.get(j)[2]);	
		}
		
	}

}