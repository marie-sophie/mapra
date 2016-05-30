package togeno;

import java.util.LinkedList;

/** gene object with annotations and intermediate scores from several diseases/ metabolites*/
public class AnnotatedGene extends Gene {

	/** intermediate score = product of (1-s) with s=score for disease annotated to this gene */
	private double currentScore;
	/** saves the current maximum score */
	private double currentMax;
	/** saves list of at most 3 disease/metabolite ids */ 
	private LinkedList<String> currentMaxIds;
	/** indicates if currentMax is a truncated list, moreMax = true -> there are more than 3 ids with max score*/
	private boolean moreMax;
	
	
	/**
	 * generates a gene object that can be annotated with results from Phenomizer/MetaboliteScore
	 * @param id id of the gene (e.g. ensembl id)
	 */
	public AnnotatedGene(String id) {
		super(id);
		currentScore = 1;
		currentMax =-1;
		currentMaxIds = new LinkedList<String>();
		moreMax=false;
	}
	
	/**
	 * adds a result from Phenomizer/Metabolite Score to the gene
	 * @param disease_id PhenoDis disease id or metabolite id
	 * @param score gene score resulting from Phenomizer prediction for the disease with id dis_or_met_id or resulting
	 * 		from MetaboliteScore for the metabolite with id dis_or_met_id
	 */
	public void add(String dis_or_met_id, double score) {
		currentScore = currentScore*(1-score);
		//new score is equal to max
		if(Math.abs(currentMax-score)<1E-5){
			//do not save more than 3 ids with max contribution
			if(currentMaxIds.size()<3){
				currentMaxIds.add(dis_or_met_id);
			}
			else{
				moreMax=true;
			}
		} else if(score>currentMax){
			currentMax=score;
			currentMaxIds=new LinkedList<String>();
			currentMaxIds.add(dis_or_met_id);
			moreMax=false;
		}
	}
	
	/**
	 * retrieves at most 3 disease/metabolite ids with maximum score annotated to this gene
	 * -> important disease/metabolite annotation
	 * @return array of disease_ids (PhenoDis) with maximum score
	 */
	public String [] getContributorIds(){
		String [] ids_return = new String [currentMaxIds.size()];
		int position=0;
		for(String disease_id:currentMaxIds){
			ids_return[position] = disease_id;
			position++;
		}
		return ids_return;
	}
	
	/**
	 * retrieves the current score summarizing all annotations to this gene (1-product of (1-s) for all annotated scores s)
	 * @return current score of the gene
	 */
	public double getFinalScore(){
		//no annotations -> final score = 0
		if(currentMaxIds.size()==0){
			return 0;
		}
		return 1-currentScore;
	}
	
	/**
	 * indicates if there are more disease/metabolite ids leading to the maximum score than saved in this object
	 * @return true if the array of getContributorIds() does not contain all disease with maximum score
	 */
	public boolean moreMaxThanListed(){
		return moreMax;
	}
	
	/**
	 * clears all disease/metabolite scores that were annotated so far to the gene
	 * method allows reuse of GeneAssociations for several runs of PhenoToGeno or MetaboToGeno
	 */
	public void resetAnnotation(){
		currentScore = 1;
		currentMax =-1;
		currentMaxIds = new LinkedList<String>();
		moreMax=false;
	}
	
}