package searchEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;



import comparators.PageRankComparator;
import comparators.RetrievedDocumentComparator;
import comparators.TFIDFComparator;
import comparators.TermVectorComparator;
import comparators.booleanANDComparator;
import comparators.cosineSimilarityComparator;
import comparators.cumulativeTFComparator;

/**
 * index of terms in the collections; arranged with a binary postings list,
 * referring to a document term-frequency vector
 * 
 * @author jncys
 *
 */
public class SearchEngine {

	// variables relating to the index;
	private double aveDocLength; 
	// posting list,the key is the word, the arrayslist is all of the documents
	// which contain the word
	private HashMap<Integer, ArrayList<Integer>> postingList = new HashMap<Integer, ArrayList<Integer>>();

	// map storing terms as ID numbers
	private HashMap<String, Integer> wordtoID = new HashMap<String, Integer>();

	// an arrayslist of all the documents
	private ArrayList<Document> documents = new ArrayList<Document>();

	// Map storing documents as ID numbers
	private HashMap<String, Integer> documentNameToID = new HashMap<String, Integer>();

	private HashMap<String, Double> pageRankScores = new HashMap<String, Double>();

	
	
	/////////////////////////////////
	// LOADING DATA
	///////////////////////////////////

	
	public void loadWeights(String fileName){
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			
			
			TFIDFCoefficient = Double.parseDouble(scanner.nextLine());
			cosineCoefficent = Double.parseDouble(scanner.nextLine());
			bm25Coeffiecient = Double.parseDouble(scanner.nextLine());
			pageRankCoeffiecent = Double.parseDouble(scanner.nextLine());
			booleanANDCoefficient = Double.parseDouble(scanner.nextLine());
			cumulativeFrequencyCoefficient = Double.parseDouble(scanner.nextLine());
			

			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * load pageRank data
	 * @param fileName
	 */
	public void loadPageRank(String fileName) {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				try {
					String[] line = scanner.nextLine().split(" ");
					String docName = line[0];
					Double score = Double.valueOf(line[1]);
					pageRankScores.put(docName, score);
					// System.out.println(score);
				} catch (Exception e) {

				}
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * load data from a file of document term vectors
	 *
	 * @param filename
	 */
	public void loadData(String filename) {
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			int documentCount = 0;
			int wordCount = 0;
			int totalwordcount= 0;

			while (scanner.hasNextLine()) {

				ArrayList<TermFrequencyPair> documentvector = new ArrayList<TermFrequencyPair>();

				String[] line = scanner.nextLine().split(" ");
				String documentName = line[0]; // name of the document.

				// documents.add(documentName); makesure not a duplicate

				if (!documentNameToID.containsKey(documentName)) {
					documentNameToID.put(documentName, documentCount);
					documentCount++;

					int documentSize = 0;

					for (int i = 1; i < line.length; i++) {
						String tuple = line[i];
						String[] pair = tuple.split(":::");
						if (pair.length == 2) {
							String word = pair[0];
							if (!wordtoID.containsKey(word)) {
								wordtoID.put(word, wordCount);
								wordCount++;
							}
							int term = wordtoID.get(word);

							if (!postingList.containsKey(term)) {
								postingList.put(term, new ArrayList<Integer>());
							}
							postingList.get(term).add(documentNameToID.get(documentName));

							try {
								int frequency = Integer.parseInt(pair[1]);
								documentSize += frequency;
								totalwordcount += frequency;
								documentvector.add(new TermFrequencyPair(term, frequency));

							} catch (Exception e) {
								//System.out.println("error" + tuple);
							}
						}
					}
					Collections.sort(documentvector, new TermVectorComparator());
					Document doc = new Document(documentName, documentSize, documentvector);
					documents.add(doc);

				}
			}
			scanner.close();
			aveDocLength = totalwordcount/documents.size();
	

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////// SEARCH METHODS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	/**
	 * return a list of queried documents ordered by pagerank
	 * @param query
	 * @return
	 */
	public ArrayList<String> pagerankSearch(String query){
		ArrayList<String>returndocuments = new ArrayList<String>();
		ArrayList<Integer>docIDs = booleanRetrievalDocID(query);
		
		ArrayList<RetrievedDocument> retrieveddocuments = new ArrayList<RetrievedDocument>();
		for (Integer docID : docIDs) {
			String docname = documents.get(docID).getDocumentName();
			double score;
			if(pageRankScores.containsKey(docname)){
				score = pageRankScores.get(docname);
			}
			else{
				score = 0;
			}

			RetrievedDocument rd = new RetrievedDocument(docname);
			rd.setRating(score);
			retrieveddocuments.add(rd);
		}
		Collections.sort(retrieveddocuments, new RetrievedDocumentComparator());
		
		for (RetrievedDocument rd : retrieveddocuments) {
			returndocuments.add(rd.Documentname);
		}	
				
		return returndocuments;
	}
	
	
	/**
	 * get the pagerank  score of a document
	 * @param doc
	 * @return
	 */
	public double getscorePageRank(Document doc){
		
		String docName = doc.getDocumentName();
		if(pageRankScores.containsKey(docName)){
			return pageRankScores.get(docName);
		}
		else{
			return 0;
		}
		
	}
	
	
	
	/**
	 * boolean or search
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<String> booleanSearch(String query) {
		// arrayList to return
		ArrayList<String> returnDocuments = new ArrayList<String>();

		// helper function to recall the list docIDs which are relevant to this
		// query
		ArrayList<Integer> docIDs = booleanRetrievalDocID(query);
		
		ArrayList<TermFrequencyPair> queryVector =queryToTermVector(query);
		
		ArrayList<RetrievedDocument>retrieveddocs = new ArrayList<RetrievedDocument>();
		
		for (Integer docID : docIDs) {
			Document doc= documents.get(docID);
			
			double score = CompareVectors.countTermFrequencies(queryVector, doc.getDocumentVector());
			RetrievedDocument rd = new RetrievedDocument(doc.getDocumentName());
			rd.setRating(score);
			retrieveddocs.add(rd);
		}
		Collections.sort(retrieveddocs, new RetrievedDocumentComparator());
		
		
		for (RetrievedDocument rd : retrieveddocs) {	
			returnDocuments.add(rd.getDocumentname());
		}	
		return returnDocuments;
	}
	
	/**
	 * boolean AND search
	 * @param query
	 * @return
	 */
	public ArrayList<String> booleanAND(String query){
		ArrayList<Integer> doclist = booleanRetrievalDocID(query);
		ArrayList<TermFrequencyPair> queryVector = queryToTermVector(query);
		ArrayList<String> returnDocuments = new ArrayList<String>();
		
		for (Integer docID : doclist) {
			String docName = documents.get(docID).getDocumentName();
			ArrayList<TermFrequencyPair> docVector = documents.get(docID).getDocumentVector();
			if(scoreBooleanAND(queryVector, docVector)==1){
				returnDocuments.add(docName);
			}
		}
		return returnDocuments;
		
	}
	/**
	 * score document by boolean and to the query
	 * @param queryVector
	 * @param docVector
	 * @return
	 */
	private double scoreBooleanAND(ArrayList<TermFrequencyPair> queryVector, ArrayList<TermFrequencyPair> docVector) {
		int querycounter = 0, doccounter = 0;

		while (querycounter < queryVector.size()) {
			if (doccounter == docVector.size()) {
				return 0;
			}

			if (queryVector.get(querycounter).getTerm() > docVector.get(doccounter).getTerm()) {
				doccounter++;
			} else if (queryVector.get(querycounter).getTerm() < docVector.get(doccounter).getTerm()) {
				return 0;
			} else {
				doccounter++;
				querycounter++;
			}
		}
		return 1;
	}
	
	


	public ArrayList<String> TF_IDF(String query) {
		ArrayList<RetrievedDocument> retrievedDocuments = new ArrayList<RetrievedDocument>();

		ArrayList<Integer> docIDs = booleanRetrievalDocID(query);
		ArrayList<TermFrequencyPair> queryVector =queryToTermVector(query);
		
		for (Integer docID : docIDs) {
			Document doc =documents.get(docID);
			double score = scoreTFIDF(queryVector, doc);
			RetrievedDocument rd  = new RetrievedDocument(doc.getDocumentName());
			rd.setRating(score);
			retrievedDocuments.add(rd);
		}
		Collections.sort(retrievedDocuments, new RetrievedDocumentComparator());
		
		ArrayList<String> returndocuments = new ArrayList<String>();
	
		for (RetrievedDocument rd : retrievedDocuments) {	
			returndocuments.add(rd.Documentname);
		}
		return returndocuments;
	}	
		

	/**
	 * score individual documents by TFIDF
	 * 
	 * @param queryVector
	 * @param doc
	 * @return
	 */
	private double scoreTFIDF(ArrayList<TermFrequencyPair> queryVector, Document doc) {
		double score = 0;

		int queryCounter = 0, docCounter = 0;

		ArrayList<TermFrequencyPair> docVector = doc.getDocumentVector();

		while (queryCounter < queryVector.size() && docCounter < docVector.size()) {
			if (queryVector.get(queryCounter).getTerm() < docVector.get(docCounter).getTerm()) {
				queryCounter++;
			} else if (queryVector.get(queryCounter).getTerm() > docVector.get(docCounter).getTerm()) {
				docCounter++;
			} else {
				int term = docVector.get(docCounter).getTerm();
				int frequency = docVector.get(docCounter).getFrequency();

				score += ((double) frequency / doc.getDocumentSize())
						* Math.log((double) documents.size() / postingList.get(term).size());
				
				docCounter++;
				queryCounter++;
			}
		}
		return score;
	}
	


	/**
	 * search rating by cosine similarity
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<String> cosineSearch(String query) {

		ArrayList<String> returnDocuments = new ArrayList<String>();
		ArrayList<RetrievedDocument> retrieveddocs = new ArrayList<RetrievedDocument>();

		// get get the query as a query vector, and get the ist of relevant
		// docuemnts
		ArrayList<TermFrequencyPair> queryVector = queryToTermVector(query);

		ArrayList<Integer> docIDs = booleanRetrievalDocID(query);
		for (Integer docID : docIDs) {
			Document doc = documents.get(docID);
			String docName = doc.getDocumentName();

			RetrievedDocument rd = new RetrievedDocument(docName);
			rd.setRating(CompareVectors.cosineSimilarity(queryVector, doc.getDocumentVector()));
			retrieveddocs.add(rd);
		}
		Collections.sort(retrieveddocs, new RetrievedDocumentComparator());

		for (RetrievedDocument doc : retrieveddocs) {
			returnDocuments.add(doc.getDocumentname());
		}

		return returnDocuments;
	}
	
	

	
	
	
	
	




//	public ArrayList<String> searchPagerank(String query) {
//		ArrayList<String> returnDocuments = new ArrayList<String>();
//		ArrayList<Integer> docIDs = booleanRetrievalDocID(query);
//		ArrayList<RetrievedDocument> docs = new ArrayList<RetrievedDocument>();
//		
//		for (Integer integer : docIDs) {
//			String documentname = documents.get(integer).getDocumentName();
//			RetrievedDocument rd = new RetrievedDocument(documentname);
//
//			if (pageRankScores.containsKey(documentname)) {
//				rd.setRating(pageRankScores.get(documentname));
//			} else {
//				//if not in the list set to default 1/size of the pageranklist
//				rd.setRating((double)1/pageRankScores.size());
//			}
//
//			docs.add(rd);
//		}
//		Collections.sort(docs, new RetrievedDocumentComparator());
//
//		for (int i = 0; i < 10; i++) {
//		}
//
//		return null;
//
//	}
	
	/**
	 * BM25 search
	 * @param query
	 * @return
	 */
	public ArrayList<String> bm25search(String query){
		ArrayList<String> returnDocuments = new ArrayList<String>();
		
		ArrayList<Integer> docIDs= booleanRetrievalDocID(query);
		ArrayList<TermFrequencyPair> queryvector = queryToTermVector(query);
		ArrayList<RetrievedDocument> retrieveddocs = new ArrayList<RetrievedDocument>();
		for (Integer docID:docIDs) {
			
			Document doc = documents.get(docID);
			double score = BM25Score(query, doc);
			RetrievedDocument rd = new RetrievedDocument(doc.getDocumentName());
			rd.setRating(score);	
			retrieveddocs.add(rd);
		}
		Collections.sort(retrieveddocs, new RetrievedDocumentComparator());
		for (RetrievedDocument retrievedDocument : retrieveddocs) {
			returnDocuments.add(retrievedDocument.Documentname);
			//System.out.println(retrievedDocument.rating);

		}
		
		return returnDocuments;
		
	}

	/**
	 * score document by bm25
	 * @param query
	 * @param doc
	 * @return
	 */
	 public double BM25Score(String query, Document doc) {
	 double score = 0;
	 // query vector
	 ArrayList<TermFrequencyPair> queryVector = queryToTermVector(query);
	 // document Vector
	 ArrayList<TermFrequencyPair> docvector = doc.getDocumentVector();
	
	 int doclength = doc.getDocumentSize();
	
	 int docCounter = 0, queryCounter = 0;
	 
	 //number of documents in the collection
	 int collectionSize = documents.size();
	 
	 while(queryCounter < queryVector.size() && docCounter < docvector.size()){
		 
		 if(queryVector.get(queryCounter).getTerm()> docvector.get(docCounter).getTerm()){
			 docCounter++; 
		 }
		 else if(queryVector.get(queryCounter).getTerm()< docvector.get(docCounter).getTerm()){ 
			 queryCounter++;
		 }
		 else{
			 
			 int term = queryVector.get(queryCounter).getTerm();
			 int freq = docvector.get(docCounter).getFrequency();	
			 int termDocCount = postingList.get(term).size();	 
			

			 
			 
			 score+= Math.max(0,Math.log((collectionSize-termDocCount +0.5)/(termDocCount+0.5))
					 *((freq*2.5)/ (freq + 1.5*(0.25 + (0.75*(doclength/aveDocLength)))))); 
			
			 docCounter++;
			 queryCounter++;
			 
		 }

	 }
	
	 return score;
	 }
	
	
	

	//////////////////////////////////////////////
	// HELPER METHODS
	////////////////////////////////////////////

	/**
	 * retrieve a list of document IDs which correspond to the query 
	 * @param query
	 * @return
	 */
	public ArrayList<Integer> booleanRetrievalDocID(String query) {
		// Hash set of documents seen according to boolean OR
		HashSet<Integer> retrievedDocuments = new HashSet<Integer>();

		// Split and stem the query words.
		String[] queryarray = query.toLowerCase().split("[^a-z]+");
		for (String word : queryarray) {
			word = Stemmer.stem(word);
			int wordID;

			// check if word in within the vocabulary
			if (wordtoID.containsKey(word) && word != null) {
				wordID = wordtoID.get(word);

				// if (postingList.containsKey(wordID) && word != null) {
				for (Integer docID : postingList.get(wordID)) {
					
					retrievedDocuments.add(docID);

				}
				// }
			}
		}
		// copy docIDs into an ArrayList to return
		ArrayList<Integer> returnDocID = new ArrayList<Integer>();
		for (Integer docID : retrievedDocuments) {
			returnDocID.add(docID);

		}
		return returnDocID;

	}

	/**
	 * turn a query into a term vector array
	 * 
	 * @param query
	 * @return
	 */
	public ArrayList<TermFrequencyPair> queryToTermVector(String query) {
		ArrayList<TermFrequencyPair> queryVector = new ArrayList<TermFrequencyPair>();
		String[] line = query.split("[^a-z]+");
		TreeMap<Integer, Integer> termFrequencies = new TreeMap<Integer, Integer>();
		for (String word : line) {
			word = Stemmer.stem(word);

			if (wordtoID.containsKey(word)) {
				int wordID = wordtoID.get(word);
				if (!termFrequencies.containsKey(wordID)) {
					termFrequencies.put(wordID, 0);
				}
				termFrequencies.replace(wordID, termFrequencies.get(wordID) + 1);

			}
		}
		for (Integer term : termFrequencies.keySet()) {
	
			queryVector.add(new TermFrequencyPair(term, termFrequencies.get(term)));

		}

		return queryVector;
	}

	
	
	
	
	
	////////////////////////////////////////////////////////////
	////// Learning to rank
	///////////////////////////////////////////////////////////

	// variables relating to the learning

	private double TFIDFCoefficient = 1;
	private double cosineCoefficent = 1;
	private double booleanANDCoefficient = 1;
	private double cumulativeFrequencyCoefficient = 1;
	private double bm25Coeffiecient = 1;
	private double pageRankCoeffiecent = 1;
	
	// double pageRankCoeffiecent = 1;

	private double TFIDFgain;
	private double cosinegain;
	private double booleanANDgain;
	private double cumulativeFrequencygain;
	private double bm25gain;
	private double pageRankgain;


	ArrayList<RetrievedDocument> retrievedDocs;

	
	
	/**
	 * search which uses the weight sum of its part
	 * @param query
	 * @return
	 */
	public ArrayList<String> learningSearch(String query) {

		// list of relevent documents boolean search
		ArrayList<Integer> docIDs = booleanRetrievalDocID(query);

		// queryVector
		ArrayList<TermFrequencyPair> queryVector = queryToTermVector(query);
//		for (TermFrequencyPair termFrequencyPair : queryVector) {
//		}

		// arralist of RetrievedDocument objects
		retrievedDocs = new ArrayList<RetrievedDocument>();

		for (Integer docID : docIDs) {
			Document doc = documents.get(docID);

			RetrievedDocument rd = new RetrievedDocument(doc.getDocumentName());

			rd.setCosineScore(CompareVectors.cosineSimilarity(queryVector, doc.getDocumentVector()));

			rd.setCumulativetermFrequency(CompareVectors.countTermFrequencies(queryVector, doc.getDocumentVector())/10);

			rd.setTFIDFScore(scoreTFIDF(queryVector, doc) +1);

			rd.setBooleanANDscore(scoreBooleanAND(queryVector, doc.getDocumentVector()));
			
			//rd.setBm25Score(BM25Score(query,doc));
			
			rd.setPageRankScore(getscorePageRank(doc));
			
			
			

			

			double totalScore = TFIDFCoefficient * rd.getTFIDFScore() + cosineCoefficent * rd.getCosineScore()
					+ cumulativeFrequencyCoefficient * rd.getCumulativetermFrequency()
					+ booleanANDCoefficient * rd.getBooleanANDscore()
					//+ bm25Coeffiecient * rd.getBm25Score()
					+ pageRankCoeffiecent * rd.getPageRankScore();
			
			rd.setRating(totalScore);
			
			retrievedDocs.add(rd);

		}
		Collections.sort(retrievedDocs, new RetrievedDocumentComparator());

		ArrayList<RetrievedDocument> top10 = new ArrayList<RetrievedDocument>();

		for (int i = 0; i < 10; i++) {
			top10.add(retrievedDocs.get(i));
			//System.out.println(retrievedDocs.get(i).getRating());
		}
		retrievedDocs = top10;

		ArrayList<String> returndocument = new ArrayList<String>();

		for (RetrievedDocument rd : retrievedDocs) {
			returndocument.add(rd.Documentname);
			//System.out.println(rd.Documentname);
		}

		return returndocument;
	}

	/**
	 * training the model and reassigning weights to the retrieval models.
	 * 
	 * @param relevances
	 */
	public void learn(ArrayList<Integer> relevances) {
		HashMap<String, Integer> relevanceScores = new HashMap<String, Integer>();

	
		for (int i = 0; i < retrievedDocs.size(); i++) {

			String docName = retrievedDocs.get(i).getDocumentname();
			Integer relevance = relevances.get(i);
			relevanceScores.put(docName, relevance);
		}

		TFIDFgain = 5.0;
		Collections.sort(retrievedDocs, new TFIDFComparator());
		for (int i = 0; i < retrievedDocs.size(); i++) {
			String doc = retrievedDocs.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				TFIDFgain += (10-i);
			}

		}
		cosinegain = 5.0;
		Collections.sort(retrievedDocs, new cosineSimilarityComparator());
		for (int i = 0; i < retrievedDocs.size(); i++) {
			String doc = retrievedDocs.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				cosinegain += (10 - i);
			}

		}
		booleanANDgain = 5.0;
		Collections.sort(retrievedDocs, new booleanANDComparator());
		for (int i = 0; i < retrievedDocs.size(); i++) {
			String doc = retrievedDocs.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				booleanANDgain += (10 - i);
			}

		}
		cumulativeFrequencygain = 5.0;
		Collections.sort(retrievedDocs, new cumulativeTFComparator());
		for (int i = 0; i < retrievedDocs.size(); i++) {
			String doc = retrievedDocs.get(i).getDocumentname();
			if (relevanceScores.get(doc).equals(1)) {
				cumulativeFrequencygain += (10 - i);
			}

		}	
		pageRankgain = 5.0;
		Collections.sort(retrievedDocs, new PageRankComparator());
		for (int i = 0; i < retrievedDocs.size(); i++) {
			String doc = retrievedDocs.get(i).getDocumentname();
			if(relevanceScores.get(doc).equals(1)) {
				pageRankgain += (10-i);
			}
		}
		
//		bm25gain = 5;
//		Collections.sort(retrievedDocs, new BM25Comparator());
//			for (int i = 0; i < retrievedDocs.size(); i++) {
//				String doc = retrievedDocs.get(i).getDocumentname();
//				if (relevanceScores.get(doc).equals(1)) {
//					bm25gain += (10 - i);
//				}
//		}
		

		double totalGain = TFIDFgain + cosinegain + booleanANDgain + cumulativeFrequencygain  + pageRankgain;
		
		
		TFIDFCoefficient *= (double) TFIDFgain / (totalGain / 5);
		
		cosineCoefficent *= (double) cosinegain/ (totalGain / 5);
		
		booleanANDCoefficient *= (double) booleanANDgain / (totalGain / 5);
		
		cumulativeFrequencyCoefficient *= (double) cumulativeFrequencygain / (totalGain / 5);
		
		pageRankCoeffiecent*=(double) pageRankgain/(totalGain / 5);
		
		//bm25Coeffiecient *= (double) bm25gain / (totalGain / 5);

		//System.out.println(TFIDFCoefficient + " " + cosineCoefficent + " " + booleanANDCoefficient + " "
		//		+ cumulativeFrequencyCoefficient);
		
		
		//save coefficents to file
		File file = new File("weights");
		try {
			FileWriter fw = new FileWriter(file,false);
			
			fw.append(TFIDFCoefficient+ "\n");
			fw.append(cosineCoefficent + "\n");
			fw.append(bm25Coeffiecient+"\n");
			fw.append(pageRankCoeffiecent+"\n");
			fw.append(booleanANDCoefficient+"\n");
			fw.append(cumulativeFrequencyCoefficient+"\n");
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	//getters for coefficents
	
	public double getTFIDFCoefficent(){
		return TFIDFCoefficient;
	}
	public double getPageRankCoefficent(){
		return pageRankCoeffiecent;
	}
	public double getConsineCoeffient(){
		return cosineCoefficent;
	}
	public double  getCumulativeFrequencyCoefficent(){
		return cumulativeFrequencyCoefficient;
	}
	public double getbooleanANDCoefficent(){
		return booleanANDCoefficient;
	}
		public double getBM25Coefficent(){
		return bm25Coeffiecient;
	}
}

