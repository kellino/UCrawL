package searchEngine;

import java.util.ArrayList;

public class CompareVectors {

	
	public static double cosineSimilarity(ArrayList<TermFrequencyPair> doc1, ArrayList<TermFrequencyPair> doc2){
		double similarity = 0;
		
		int doc1counter = 0, doc2counter = 0;
		
		
		double numerator = 0, doc1distance = 0, doc2distance = 0;
		
		while(doc1counter <doc1.size() && doc2counter < doc2.size()){
			if(doc1.get(doc1counter).getTerm() < doc2.get(doc2counter).getTerm()){
				doc1counter++;
			}
			
			else if(doc1.get(doc1counter).getTerm() > doc2.get(doc2counter).getTerm()){
				doc2counter++;
			}
			else{
				numerator += doc1.get(doc1counter).getFrequency() * doc2.get(doc2counter).getFrequency();
				doc1counter++;
				doc2counter++;
			}	
		}
		for (int i = 0; i < doc1.size(); i++) {
			doc1distance += doc1.get(i).getFrequency() * doc1.get(i).getFrequency();
		}
		doc1distance = Math.sqrt(doc1distance);
		
		for (int i = 0; i < doc2.size(); i++) {
			doc2distance += doc2.get(i).getFrequency() * doc2.get(i).getFrequency();
		}
		doc2distance = Math.sqrt(doc2distance);
		
		similarity = numerator/(doc1distance * doc2distance);

		return similarity;
	}
	
	
	/**
	 * return the number of times the terms in a query occur in a document
	 * @param queryVector
	 * @param docVector
	 * @return
	 */
	public static int countTermFrequencies(ArrayList<TermFrequencyPair> queryVector, ArrayList<TermFrequencyPair>docVector){
		
		int count = 0;
		int querycounter = 0, doccounter = 0;
		

		
		while(querycounter <queryVector.size() && doccounter < docVector.size()){
			if(queryVector.get(querycounter).getTerm() < docVector.get(doccounter).getTerm()){
				querycounter++;
			}
			
			else if(queryVector.get(querycounter).getTerm() > docVector.get(doccounter).getTerm()){
				doccounter++;
			}
			else{
				count+= docVector.get(doccounter).getFrequency();
				querycounter++;
				doccounter++;
			}	
		}

		return count;
	}
}
