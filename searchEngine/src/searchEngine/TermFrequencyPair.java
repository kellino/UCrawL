package searchEngine;

public class TermFrequencyPair {
	private int term;
	private int frequency;
	
	public TermFrequencyPair(int term, int frequency){
		this.term = term;
		this.frequency = frequency;
		
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
}
