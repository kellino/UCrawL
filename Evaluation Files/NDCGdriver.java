package uk.ac.ucl.irdm.ndcg;

import java.util.ArrayList;

public class NDCGdriver {

	public static void main(String[] args){
		NDCG NDCG = new NDCG();
		NDCG.readBM25ToHashMap();
		NDCG.readQrelsToGuavaTable();
		
		//ArrayList<ArrayList<Double>> averageNDCGatK = new ArrayList<ArrayList<Double>>();
		double[][] averageNDCGatK = new double[10][20];
		for (int i=1; i<=20; i++){
			
			ArrayList<Double> idealRelevanceListForQ = NDCG.getIdealRelevanceList(Integer.toString(i));
			ArrayList<Double> realRelevanceListForQ = NDCG.getRealRelevanceList(Integer.toString(i));
			ArrayList<Double> idcgValsListForQ= NDCG.calcDCG(idealRelevanceListForQ);
			ArrayList<Double> dcgValsListForRealQ = NDCG.calcDCG(realRelevanceListForQ);
			
			int[] rangesToCalcNDCG =  {1,2,3,4,5,6,7,8,9,10};
			System.out.println("Query Topic: "+i+" (ucl_site_search)");
			System.out.println("K | NDCG@K");
			
			for(int j=0; j<rangesToCalcNDCG.length; j++){
				
				System.out.println(rangesToCalcNDCG[j]+" | "+ NDCG.calcNDCGatK(dcgValsListForRealQ, idcgValsListForQ, rangesToCalcNDCG[j]));
				averageNDCGatK[j][i-1]= NDCG.calcNDCGatK(dcgValsListForRealQ, idcgValsListForQ, rangesToCalcNDCG[j]);
			} 
			System.out.println("");
		
		}
		System.out.println("Average NDCG@K:");
		
		for (int i=0; i<averageNDCGatK.length; i++){
			
			double sum = 0;
		    for (int j = 0; j < averageNDCGatK[i].length; j++){
		      sum = sum + averageNDCGatK[i][j];
		    }
		    // calculate average
		    double average = sum / averageNDCGatK[i].length;

		    System.out.println("K = "+(i+1)+": " + average);
		} 
		
	}

}
