package searchEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;


import comparators.RetrievedDocumentComparator;

public class PostingListIndex {
	// the postingsList index!!
	HashMap<String, PostingList> postingsIndex;
	
	
	//map of the documentID to document name
	HashMap<Integer, String> IdToDocName;
	
	
	HashMap<Integer,Integer> docLength;
	int documentCount;

	// constructor
	public PostingListIndex() {
		this.postingsIndex = new HashMap<String, PostingList>(1700000);
		this.IdToDocName = new HashMap<Integer, String>(30000);
		this.documentCount = 0;
		
		//map documentID to document length
		this.docLength = new HashMap<Integer, Integer>();
	}

	public void addDocument(String documentName, String documentBody) {

		IdToDocName.put(documentCount, documentName);

		// tokenise the document body.
		String[] tokenList = documentBody.toLowerCase().split("[^a-z]+");
		
		HashMap<String, Integer> termFrequencies = new HashMap<String, Integer>();
		
		//calculate the frequencies of the tokens in the document
		int tokenCount = 0;
		for (String word : tokenList) {
			//stemmer may give a null token if a stopword etc
			String token = Stemmer.stem(word);
			if (token != null) {
				if (!termFrequencies.containsKey(token)) {
					termFrequencies.put(token, 0);
				}
				termFrequencies.replace(token, termFrequencies.get(token) + 1);
				tokenCount++;
			}
		}
		
		docLength.put(documentCount, tokenCount);
		//now put the term frequencies into the main index
		for (String string : termFrequencies.keySet()) {
			if (!postingsIndex.containsKey(string)) {
				postingsIndex.put(string, new PostingList(string));
			}
			postingsIndex.get(string).addpost(new Post(documentCount, termFrequencies.get(string)));
		}
		// increment the document count
		documentCount++;
	}
	
	/**
	 * return the size of the vocabulary
	 * @return
	 */
	public int getVocabularySize(){
		return postingsIndex.size();
	}
	
	
	/**
	 * boolean search
	 * ranked by term frequency
	 * @param query
	 * @return
	 */
	public ArrayList<String> booleanOR(String query){
		
		TreeMap<Integer,RetrievedDocument> retrieved = new TreeMap<Integer,RetrievedDocument>();
		String[] queries = query.toLowerCase().split("[^a-z]+");
		
		for (String string : queries) {
			
			String token = Stemmer.stem(string);
			
			if(token != null && postingsIndex.containsKey(token)){
				ArrayList<Post> postlist = postingsIndex.get(token).getPostList();
				
				for (Post post :postlist ) {
					if(!retrieved.containsKey(post.getDocumentID())){					
						retrieved.put(post.getDocumentID(), new RetrievedDocument(IdToDocName.get(post.getDocumentID())));	
					}
					retrieved.get(post.getDocumentID()).incrementRating(post.getFrequency());				
				}
			}		
		}
		ArrayList<RetrievedDocument> sortinglist = new ArrayList<RetrievedDocument>();
		
		for (Integer docID : retrieved.keySet()) {
			sortinglist.add(retrieved.get(docID));
		}
		Collections.sort(sortinglist,new RetrievedDocumentComparator());
		
		
		ArrayList<String> output = new ArrayList<String>();
		for (RetrievedDocument doc : sortinglist) {
			System.out.println(doc.Documentname + " "+ doc.getRating());
			output.add(doc.Documentname);
		}
		return output;	
	}
	
	
	
	public ArrayList<String> booleanAND(String query){
		
		TreeMap<Integer,RetrievedDocument> retrieved = new TreeMap<Integer,RetrievedDocument>();
		String[] queries = query.toLowerCase().split("[^a-z]+");
		
		for (String string : queries) {
			String token = Stemmer.stem(string);
			
			if(token != null && postingsIndex.containsKey(token)){
				ArrayList<Post> postlist = postingsIndex.get(token).getPostList();
				
				for (Post post :postlist ) {
					if(!retrieved.containsKey(post.getDocumentID())){					
						retrieved.put(post.getDocumentID(), new RetrievedDocument(IdToDocName.get(post.getDocumentID())));	
					}
					retrieved.get(post.getDocumentID()).incrementRating(post.getFrequency());				
				}
			}		
		}
		ArrayList<RetrievedDocument> sortinglist = new ArrayList<RetrievedDocument>();
		
		for (Integer docID : retrieved.keySet()) {
			sortinglist.add(retrieved.get(docID));
		}
		Collections.sort(sortinglist,new RetrievedDocumentComparator());
		
		
		ArrayList<String> output = new ArrayList<String>();
		for (RetrievedDocument doc : sortinglist) {
			System.out.println(doc.Documentname + " "+ doc.getRating());
			output.add(doc.Documentname);
		}
		return output;	
	}
	
	
	
	
	
	
	/**
	 * tfidf
	 * @param query
	 * @return
	 */
	public ArrayList<String> TF_IDF(String query){
		
		TreeMap<Integer,RetrievedDocument> retrieved = new TreeMap<Integer,RetrievedDocument>();
		String[] queries = query.toLowerCase().split("[^a-z]+");
		
		for (String string : queries) {
			String token = Stemmer.stem(string);
			
			if(token != null && postingsIndex.containsKey(token)){
				ArrayList<Post> postlist = postingsIndex.get(token).getPostList();
				
				for (Post post :postlist ) {
					if(!retrieved.containsKey(post.getDocumentID())){					
						retrieved.put(post.getDocumentID(), new RetrievedDocument(IdToDocName.get(post.getDocumentID())));	
					}
					double score = ((double)post.getFrequency()/docLength.get(post.getDocumentID())) * Math.log(documentCount/postlist.size());
					//System.out.println(post.getFrequency() + " " + IdToDocName.get(post.getDocumentID()));
					if(score >0){
						retrieved.get(post.getDocumentID()).incrementRating(score);			
					}
					
				}
			}		
		}
		ArrayList<RetrievedDocument> sortinglist = new ArrayList<RetrievedDocument>();
		
		for (Integer docID : retrieved.keySet()) {
			sortinglist.add(retrieved.get(docID));
		}
		Collections.sort(sortinglist,new RetrievedDocumentComparator());
		
		
		ArrayList<String> output = new ArrayList<String>();
		for (RetrievedDocument doc : sortinglist) {
			System.out.println(doc.Documentname + " "+ doc.getRating());
			output.add(doc.Documentname);
		}
		System.out.println(output.size());
		return output;	
	}
	
}
