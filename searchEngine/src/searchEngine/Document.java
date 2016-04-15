package searchEngine;

import java.util.ArrayList;

public class Document {

	
	
	
	private String documentName;
	private int documentSize;
	private ArrayList<TermFrequencyPair> documentVector = new ArrayList<TermFrequencyPair>();
	private ArrayList<TermFrequencyPair>firstLineVector = new ArrayList<TermFrequencyPair>();


	public Document(String docName){
		this.documentName = docName;
	}

	public Document(String documentName,int documentSize, ArrayList<TermFrequencyPair> documentVector) {
		super();
		this.documentName = documentName;
		this.documentSize = documentSize;
		this.documentVector = documentVector;
	
	}
	
	public String getDocumentName() {
		return documentName;
	}


	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}


	public int getDocumentSize() {
		return documentSize;
	}


	public void setDocumentSize(int numberOfWords) {
		this.documentSize = numberOfWords;
	}


	public ArrayList<TermFrequencyPair> getDocumentVector() {
		return documentVector;
	}


	public void setDocumentVector(ArrayList<TermFrequencyPair> documentVector) {
		this.documentVector = documentVector;
	}
	
	public ArrayList<TermFrequencyPair> getFirstLineVector() {
		return firstLineVector;
	}

	public void setFirstLineVector(ArrayList<TermFrequencyPair> firstLineVector) {
		this.firstLineVector = firstLineVector;
	}
	
}
