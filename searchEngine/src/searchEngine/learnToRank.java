package searchEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



import comparators.TFIDFComparator;
import comparators.booleanANDComparator;
import comparators.cosineSimilarityComparator;


public class learnToRank {
	ArrayList<RetrievedDocument> list;

	double TFIDFCoefficient = 1;
	double cosineCoefficent = 1;
	double booleanANDCoefficient = 1;
	double cumulativeFrequencyCoefficient = 1;

	double TFIDFgain;
	double cosinegain;
	double booleanANDgain;
	double cumulativeFrequencygain;
	
	public learnToRank(ArrayList<RetrievedDocument>list){
		this.list = list;
	}

	public void rank(ArrayList<Integer> relevances) {
		HashMap<String, Integer> relevanceScores = new HashMap<String, Integer>();

		for (int i = 0; i < list.size(); i++) {
			String docName = list.get(i).getDocumentname();
			Integer relevance = relevances.get(i);
			relevanceScores.put(docName, relevance);
		}

		TFIDFgain=0.0;
		Collections.sort(list, new TFIDFComparator());
		for (int i = 0; i < list.size(); i++) {
			String doc = list.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				TFIDFgain += (10 - i);
			}
			if (i == 10) {
				break;
			}

		}
		cosinegain=0.0;
		Collections.sort(list, new cosineSimilarityComparator());
		for (int i = 0; i < list.size(); i++) {
			String doc = list.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				cosinegain += (10 - i);
			}
			if (i == 10) {
				break;
			}

		}
		booleanANDgain = 0.0;
		Collections.sort(list, new booleanANDComparator());
		for (int i = 0; i < list.size(); i++) {
			String doc = list.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				booleanANDgain += (10 - i);
			}
			if (i == 10) {
				break;
			}

		}
		cumulativeFrequencygain=0.0;
		Collections.sort(list, new booleanANDComparator());
		for (int i = 0; i < list.size(); i++) {
			String doc = list.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				cumulativeFrequencygain += (10 - i);
			}
			if (i == 10) {
				break;
			}

		}
		System.out.println(TFIDFgain);
		System.out.println(cosinegain);
		System.out.println(booleanANDgain);
		System.out.println(cumulativeFrequencygain);
		System.out.println(TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain);
		
		
		TFIDFCoefficient *= (double)TFIDFgain/((TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain)/4);
		cosineCoefficent *= (double)cosinegain/((TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain)/4);
		booleanANDCoefficient*=(double)booleanANDgain/((TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain)/4);
		cumulativeFrequencyCoefficient *= (double)cumulativeFrequencygain/((TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain)/4);
		
		System.out.println(TFIDFCoefficient + " " + cosineCoefficent + " " + booleanANDCoefficient + " " + cumulativeFrequencyCoefficient);
	}
}
