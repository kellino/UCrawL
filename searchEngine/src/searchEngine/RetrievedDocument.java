package searchEngine;

public class RetrievedDocument {
	
	String Documentname;
	int documentID;
	
	int cumulativetermFrequency;
	double cosineScore;
	double TFIDFScore;
	double booleanANDscore;
	double pageRankScore;
	double bm25Score;


	double rating;
	
	


	public RetrievedDocument(String documentname) {
		Documentname = documentname;
		this.rating = 0.0;
		
	}

	
	
	
	//getters and setters
	
	public String getDocumentname() {
		return Documentname;
	}
	public void setDocumentname(String documentname) {
		Documentname = documentname;
	}
	public int getDocumentID() {
		return documentID;
	}
	public void setDocumentID(int documentID) {
		this.documentID = documentID;
	}
	public int getCumulativetermFrequency() {
		return cumulativetermFrequency;
	}
	public void setCumulativetermFrequency(int cumulativetermFrequency) {
		this.cumulativetermFrequency = cumulativetermFrequency;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public double getCosineScore() {
		return cosineScore;
	}
	public void setCosineScore(double cosineScore) {
		this.cosineScore = cosineScore;
	}
	
	
	public void incrementFrequency(int number){
		this.cumulativetermFrequency += number;
	}
	
	public void incrementRating(double number){
		this.rating += number;
	}
	
	public double getTFIDFScore() {
		return TFIDFScore;
	}
	public void setTFIDFScore(double tFIDFScore) {
		TFIDFScore = tFIDFScore;
	}
	
	public double getBooleanANDscore() {
		return booleanANDscore;
	}

	public void setBooleanANDscore(double d) {
		this.booleanANDscore = d;
	}
	
	public double getPageRankScore() {
		return pageRankScore;
	}

	public void setPageRankScore(double pageRankScore) {
		this.pageRankScore = pageRankScore;
	}
	
	public double getBm25Score() {
		return bm25Score;
	}

	public void setBm25Score(double bm25Score) {
		this.bm25Score = bm25Score;
	}
	
	

}
