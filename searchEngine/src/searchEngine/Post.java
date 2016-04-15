package searchEngine;

public class Post {
	private int documentID;
	private int frequency;
	
	public Post(int document, int frequency){
		this.documentID = document;
		this.frequency = frequency;
	}

	public int getDocumentID() {
		return documentID;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
