package uk.ac.ucl.irdm.ndcg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class NDCG {

	protected Map<String, ArrayList<ResultLine>> resultLinesHashMap = new HashMap<String, ArrayList<ResultLine>>();
	protected Table<String, String, Double> QDocRelevanceTable = HashBasedTable.create();
	private String pathToAdhocRetrievalResults = "posttraining_rankings.txt";
	private File adhocRetrievalResultsFile = new File(pathToAdhocRetrievalResults);
	private String pathToAdhocQrels = "qrels.posttraining";
	private File adhocQRelsFile = new File(pathToAdhocQrels);
	
	private double idealRankingsMatrix[][] = new double[20][10];

	
	public NDCG(){
		String pathToideal = "ideal_relevances_4.txt";
		double idealRanking;
		File idealRelevances = new File(pathToideal);
		BufferedReader in = null;
		try {
			//try reading the file.
			in = new BufferedReader(new FileReader(idealRelevances));
			String read = null;
			int counter = 0;
			while ((read = in.readLine()) != null) {
				String[] tokenInLine = read.split("\\s+");
				int arrayRow = Integer.parseInt(tokenInLine[0])-1;
				int arrayCol = counter-arrayRow*10;
				idealRanking = Double.parseDouble(tokenInLine[1]);
				idealRankingsMatrix[arrayRow][arrayCol]=idealRanking;
				counter++;
			}
		} catch (IOException e) {
			System.out.println("There was a problem: " + e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
		
	}
	
	/**
	 * Reads lines from the adhoc retrieval results and populates a hashmap of QueryID:list of resultLine objects.
	 */
	protected void readBM25ToHashMap(){

		String currentQueryID;
		String queryID = null;
		String documentID;
		int rank;
		currentQueryID = "1";
		ArrayList<ResultLine> resultsForQuery = new ArrayList<ResultLine>(10);

		BufferedReader in = null;
		try {
			//try reading the file.
			in = new BufferedReader(new FileReader(adhocRetrievalResultsFile));
			String read = null;

			while ((read = in.readLine()) != null) {

				String[] arrayOfLines = read.split("\\n");
				for (int i=0; i<arrayOfLines.length; i++) {

					//for each line in the array of lines split it into words:
					String[] tokenInLine = arrayOfLines[i].split("\\s+");
					//take the first word as the QueryID
					queryID = tokenInLine[0];
					documentID = tokenInLine[2];
					rank = Integer.parseInt(tokenInLine[3]); 

					if (queryID.equals(currentQueryID)){
						//If we're still on the same query ID keep adding to the results arraylist
						//						TEst: System.out.println("same query "+queryID+" "+currentQueryID + queryID.equals(currentQueryID));
						resultsForQuery.add(new ResultLine(queryID, documentID, rank, 0.0));
					} else {
						//If the query has changed, put the arraylist into the hashmap, clear the list and add the first of the new query lines.
						//						Test:System.out.println("NOT SAME query "+queryID+" "+currentQueryID);
						//listOfQueryIDs.add(queryID);
						if (!resultLinesHashMap.containsKey(queryID)) {
							resultLinesHashMap.put(currentQueryID, resultsForQuery);

							// FOR TEST ONLY
							//							for (int j=0; j< resultLinesHashMap.get(currentQueryID).size(); j++){
							//								System.out.println(resultLinesHashMap.get(currentQueryID).get(j).bm25+", "+resultLinesHashMap.get(currentQueryID).get(j).rank);
							//							}
						}
						resultsForQuery = new ArrayList<ResultLine>(10);
						resultsForQuery.add(new ResultLine(queryID, documentID, rank, 0.0));
					}

					currentQueryID = queryID;
					//					Test:System.out.println("current is now "+currentQueryID);
				}
			}
			//put final set of query results in the hash map and the list of queryIDs.
			//listOfQueryIDs.add(queryID);
			if (!resultLinesHashMap.containsKey(queryID)) {
				resultLinesHashMap.put(currentQueryID, resultsForQuery);
				// FOR TEST ONLY
				//				for (int j=0; j< resultLinesHashMap.get(currentQueryID).size(); j++){
				//					System.out.println(resultLinesHashMap.get(currentQueryID).get(j).bm25+", "+resultLinesHashMap.get(currentQueryID).get(j).rank+" FINAL");
				//				}
			}

		} catch (IOException e) {
			System.out.println("There was a problem: " + e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}

	}


	protected void readQrelsToGuavaTable(){
		String queryID="";
		String documentID="";
		double relJudgement = 0.0;
		BufferedReader in = null;
		try {
			//try reading the file.
			in = new BufferedReader(new FileReader(adhocQRelsFile));
			String read = null;

			while ((read = in.readLine()) != null) {

				String[] arrayOfLines = read.split("\\n");
				for (int i=0; i<arrayOfLines.length; i++) {

					//for each line in the array of lines split it into words:
					String[] tokenInLine = arrayOfLines[i].split("\\s+");
					//take the first word as the QueryID
					queryID = tokenInLine[0];
					documentID = tokenInLine[2];
					relJudgement = Double.parseDouble(tokenInLine[3]); 

					if (!QDocRelevanceTable.contains(queryID, documentID)) {
						QDocRelevanceTable.put(queryID, documentID, relJudgement);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("There was a problem: " + e);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}


	protected ArrayList<Double> getRealRelevanceList(String queryID){
		ArrayList<Double> realRelevanceList = new ArrayList<Double>(1100);
		for (ResultLine resultLine : resultLinesHashMap.get(queryID)){
			//add the real relevance judgement relating to the document-query pair in the result set. IN ORDER!
			if(QDocRelevanceTable.get(queryID, resultLine.documentID) == null){
				realRelevanceList.add((double)0.0);
			} else {
				realRelevanceList.add(QDocRelevanceTable.get(queryID, resultLine.documentID));
			}
		}
		return realRelevanceList;
	}


	protected ArrayList<Double> getIdealRelevanceList(String queryID){
		ArrayList<Double> idealRelevances = new ArrayList<Double>();
		
		double[] idealrels = idealRankingsMatrix[Integer.parseInt(queryID)-1];
		//Map<String, Double> map = QDocRelevanceTable.rowMap().get(queryID);
		//Source: http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
		for ( double relScore : idealrels)
		{
			//		    System.out.println(entry.getKey() + "/" + entry.getValue());
			idealRelevances.add(relScore);
		}

		Collections.sort(idealRelevances, Collections.reverseOrder());
		return idealRelevances;
	}

	protected ArrayList<Double> calcDCG(ArrayList<Double> relevanceList){
		ArrayList<Double> dcgList = new ArrayList<Double>();
		//The first element remains unchanged:
		dcgList.add(relevanceList.get(0));
		for (int i=1; i<relevanceList.size(); i++){
			double dcg = (double)relevanceList.get(i) / ((double) Math.log((double)(i+1)) / (double)Math.log(2));
			dcgList.add(dcg);
		}
		return dcgList;

	}

	//DEPRECATED (WRONG)
	protected ArrayList<Double> calcNDCG(ArrayList<Double> dcgOfRealRankings, ArrayList<Double> dcgOfIdealRankings){
		ArrayList<Double> idcgList = new ArrayList<Double>();
		double dcgOfIdealRanking = 0.0;
		for(int i=0; i<dcgOfRealRankings.size(); i++){
			try {
				if(dcgOfIdealRankings.get(i) == null || dcgOfIdealRankings.get(i) == 0.0){
					dcgOfIdealRanking = 1.0;
				} else {
					dcgOfIdealRanking = dcgOfIdealRankings.get(i);
				}
			} catch (Exception e) {
				// TODO: handle exception
				dcgOfIdealRanking = 1.0;
			}
			double idcgOfi = dcgOfRealRankings.get(i)/dcgOfIdealRanking;		
			idcgList.add(idcgOfi);
		}
		return idcgList;
	}

	//DEPRECATED (WRONG)
	protected void addNDCGListToResultLine(String queryID, ArrayList<Double> ndcgListForQuery){
		ArrayList<ResultLine> listOfResultLines = resultLinesHashMap.get(queryID);

		for (int i = 0; i<listOfResultLines.size(); i++){
			listOfResultLines.get(i).ndcg = ndcgListForQuery.get(i);
		}
	}

	//DEPRECATED (WRONG)
	protected void printResultsToConsole(){
		Set<String> mapKeys = resultLinesHashMap.keySet();
		//Source: http://stackoverflow.com/questions/46898/how-to-efficiently-iterate-over-each-entry-in-a-map
		for (String key : mapKeys){
			ArrayList<ResultLine> resultsList = resultLinesHashMap.get(key);
			for (ResultLine resultLine : resultsList){
				System.out.println(resultLine.queryID+" Q0 "+resultLine.documentID+" "+new DecimalFormat("0.000").format(resultLine.ndcg));
			} 
		}
		
		
	}
	
	
	
	protected double calcNDCGatK (ArrayList<Double> dcgOfRealRankings, ArrayList<Double> dcgOfIdealRankings, int k){
		double sumRealDCGvals=0;
		double sumIDCGvals=0;
		for (int i=0; i<k; i++){
			sumRealDCGvals+=dcgOfRealRankings.get(i);
			//System.out.println("real DCG of "+i+": "+dcgOfRealRankings.get(i));
			sumIDCGvals+=dcgOfIdealRankings.get(i);
			//System.out.println("Ideal DCG of "+i+": "+dcgOfIdealRankings.get(i));
			}
		return sumRealDCGvals/sumIDCGvals;
	}
	



}

class ResultLine {
	protected String queryID;
	protected String documentID;
	protected int rank;
	protected double ndcg;//should be formated to 3dp when written to file.
	public ResultLine(String queryID, String documentID, int rank, double ndcg){
		this.queryID = queryID;
		this.documentID = documentID;
		this.rank = rank;
		this.ndcg = ndcg;
	}
}
