package geneticnetwork;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import geneticnetwork.algorithm.DataTransformerGeneticNetwork;
import geneticnetwork.algorithm.MatrixVectorBuilder;
import geneticnetwork.algorithm.NetworkScoreAlgorithm;
import geneticnetwork.algorithm.NetworkScoreDriver;
import geneticnetwork.algorithm.RandomWalkWithRestartFixedIterations;
import geneticnetwork.algorithm.RandomWalkWithRestartUntilConvergence;
import geneticnetwork.io.FileUtilitiesGeneticNetwork;
import io.FileInputReader;
import togeno.ScoredGene;

public class TestGeneticNetworkScore {
	
	private String [][] networkFromFile;
	private HashMap<String, Double> scoresFromFile;
	
	private double [] scoresExp;
	private String [] genesExp;
	private double [] enrichExp;

	@Test
	public void testCase1WithoutDriver() {
		prepareDataForCase(1);
		NetworkScoreAlgorithm n1= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartFixedIterations(0.9,3));
		LinkedList<ScoredGene> res1 = n1.runNetworkScoreAlgorithm();
		compareToExpected(res1);
	}
	
	@Test
	public void testCase2WithoutDriver() {
		prepareDataForCase(2);
		NetworkScoreAlgorithm n2= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartFixedIterations(0.5,1));
		LinkedList<ScoredGene> res2 = n2.runNetworkScoreAlgorithm();
		compareToExpected(res2);
	}
	
	@Test
	public void testCase3WithoutDriver() {
		prepareDataForCase(3);
		NetworkScoreAlgorithm n3= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartFixedIterations(0.2,2));
		LinkedList<ScoredGene> res3 = n3.runNetworkScoreAlgorithm();
		compareToExpected(res3);
	}
	
	@Test
	public void testCase4WithoutDriver() {
		prepareDataForCase(4);
		NetworkScoreAlgorithm n4= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartUntilConvergence(0.9));
		LinkedList<ScoredGene> res4 = n4.runNetworkScoreAlgorithm();
		compareToExpected(res4);
	}
	
	@Test
	public void testCase5WithoutDriver() {
		prepareDataForCase(5);
		NetworkScoreAlgorithm n5= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartUntilConvergence(0.5));
		LinkedList<ScoredGene> res5 = n5.runNetworkScoreAlgorithm();
		compareToExpected(res5);
	}
	
	@Test
	public void testCase6WithoutDriver() {
		prepareDataForCase(6);
		NetworkScoreAlgorithm n6= new NetworkScoreAlgorithm(getBuilder(), new RandomWalkWithRestartUntilConvergence(0.2));
		LinkedList<ScoredGene> res6 = n6.runNetworkScoreAlgorithm();
		compareToExpected(res6);
	}
	
	@Test
	public void testCase14WithoutDriver(){
		prepareDataForCase(1);
		MatrixVectorBuilder mvb = getBuilder();
		NetworkScoreAlgorithm n1= new NetworkScoreAlgorithm(mvb, new RandomWalkWithRestartFixedIterations(0.9,3));
		LinkedList<ScoredGene> res1 = n1.runNetworkScoreAlgorithm();
		compareToExpected(res1);
		prepareDataForCase(4);
		NetworkScoreAlgorithm n2 = new NetworkScoreAlgorithm(getBuilder(mvb), new RandomWalkWithRestartUntilConvergence(0.9));
		LinkedList<ScoredGene> res2 = n2.runNetworkScoreAlgorithm();
		compareToExpected(res2);		
	}
	
	@Test
	public void testCase25WithoutDriver(){
		prepareDataForCase(2);
		MatrixVectorBuilder mvb = getBuilder();
		NetworkScoreAlgorithm n1= new NetworkScoreAlgorithm(mvb, new RandomWalkWithRestartFixedIterations(0.5,1));
		LinkedList<ScoredGene> res1 = n1.runNetworkScoreAlgorithm();
		compareToExpected(res1);
		prepareDataForCase(5);
		NetworkScoreAlgorithm n2 = new NetworkScoreAlgorithm(getBuilder(mvb), new RandomWalkWithRestartUntilConvergence(0.5));
		LinkedList<ScoredGene> res2 = n2.runNetworkScoreAlgorithm();
		compareToExpected(res2);
	}
	
	@Test
	public void testCase36WithoutDriver(){
		prepareDataForCase(3);
		MatrixVectorBuilder mvb = getBuilder();
		NetworkScoreAlgorithm n1= new NetworkScoreAlgorithm(mvb, new RandomWalkWithRestartFixedIterations(0.2,2));
		LinkedList<ScoredGene> res1 = n1.runNetworkScoreAlgorithm();
		compareToExpected(res1);
		prepareDataForCase(6);
		NetworkScoreAlgorithm n2 = new NetworkScoreAlgorithm(getBuilder(mvb), new RandomWalkWithRestartUntilConvergence(0.2));
		LinkedList<ScoredGene> res2 = n2.runNetworkScoreAlgorithm();
		compareToExpected(res2);
	}
	
	@Test
	public void testCase1WithDriver(){
		prepareDataForCase(1);
		NetworkScoreDriver n1= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n1.SetNetworkScoreAlgorithm(0.9, false, 3);
		LinkedList<ScoredGene> res1 = n1.runNetworkScoreAlgorithm();
		compareToExpected(res1);
	}
	
	@Test
	public void testCase2WithDriver(){
		prepareDataForCase(2);
		NetworkScoreDriver n2= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n2.SetNetworkScoreAlgorithm(0.5, false, 1);
		LinkedList<ScoredGene> res2 = n2.runNetworkScoreAlgorithm();
		compareToExpected(res2);
	}
	
	@Test
	public void testCase3WithDriver(){
		prepareDataForCase(3);
		NetworkScoreDriver n3= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n3.SetNetworkScoreAlgorithm(0.2, false, 2);
		LinkedList<ScoredGene> res3 = n3.runNetworkScoreAlgorithm();
		compareToExpected(res3);
	}
	
	@Test
	public void testCase4WithDriver(){
		prepareDataForCase(4);
		NetworkScoreDriver n4= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n4.SetNetworkScoreAlgorithm(0.9, true, -1);
		LinkedList<ScoredGene> res4 = n4.runNetworkScoreAlgorithm();
		compareToExpected(res4);
	}
	
	@Test
	public void testCase5WithDriver(){
		prepareDataForCase(5);
		NetworkScoreDriver n5= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n5.SetNetworkScoreAlgorithm(0.5, true, -1);
		LinkedList<ScoredGene> res5 = n5.runNetworkScoreAlgorithm();
		compareToExpected(res5);
	}
	
	@Test
	public void testCase6WithDriver(){
		prepareDataForCase(6);
		NetworkScoreDriver n6= new NetworkScoreDriver(networkFromFile, scoresFromFile);
		n6.SetNetworkScoreAlgorithm(0.2, true, -1);
		LinkedList<ScoredGene> res6 = n6.runNetworkScoreAlgorithm();
		compareToExpected(res6);
	}
	
	private void prepareDataForCase(int testCase){
		
		//read network edges
		boolean weight=false;
		if(testCase==2|| testCase==3 || testCase==5 || testCase==6){
			weight = true;
		}
		networkFromFile = FileUtilitiesGeneticNetwork.readEdges("../TestData/GeneticNetwork/MTGNetwork.txt", weight);
		
		//read scores
		String fileScores="";
		if(testCase==1|| testCase==2 || testCase==4 || testCase==5){
			fileScores="../TestData/GeneticNetwork/Input_PTG1.txt";
		}
		else if(testCase==3 || testCase==6){
			fileScores="../TestData/GeneticNetwork/Input_PTG2.txt";
		}
		scoresFromFile = FileUtilitiesGeneticNetwork.readGeneScoresFrom(fileScores);
		
		//read expected results
		LinkedList<String> exp = FileInputReader.readAllLinesFrom(
				"../TestData/GeneticNetwork/ExpectedRes/res"+testCase+".txt");
		scoresExp = new double[exp.size()];
		genesExp = new String[exp.size()];
		enrichExp = new double[exp.size()];
		int pos=0;
		for(String line:exp){
			String [] split = line.split("\t");
			scoresExp[pos]=Double.parseDouble(split[1]);
			genesExp[pos] = split[0];
			if(split.length<3){
				enrichExp[pos]=Double.NEGATIVE_INFINITY;
			}
			else{
				enrichExp[pos]=Double.parseDouble(split[2]);
			}
			pos++;
		}
		
	}
	
	private MatrixVectorBuilder getBuilder(){
		DataTransformerGeneticNetwork dt = new DataTransformerGeneticNetwork();
		return new MatrixVectorBuilder(dt.transformEdges(networkFromFile), dt.transformGeneScores(scoresFromFile));
	}
	
	private MatrixVectorBuilder getBuilder(MatrixVectorBuilder builder){
		DataTransformerGeneticNetwork dt = new DataTransformerGeneticNetwork();
		return new MatrixVectorBuilder(dt.transformGeneScores(scoresFromFile), builder);
	}
	
	private void compareToExpected(LinkedList<ScoredGene> g){
		int pos=0;
		assertEquals("Result size is incorrect", genesExp.length, g.size());
		double sum=0;
		for(ScoredGene gene:g){
			assertEquals("Gene id at position "+pos+" is incorrect", genesExp[pos], gene.getId());
			assertEquals("Score at position "+pos+" is incorrect", scoresExp[pos], gene.getScore(),1E-9);
			assertEquals("Enrichment score at position "+pos+" is incorrectment", enrichExp[pos],
					gene.getEnrichmentScore(g.size()),1E-10);
			sum+=gene.getScore();
			pos++;
		}
		assertEquals("Scores do not sum up to 1", 1, sum, 1E-8);
	}

}
