package comparators;

import java.util.Comparator;

import searchEngine.TermFrequencyPair;

public class TermVectorComparator implements Comparator<TermFrequencyPair>{
	
	
	//sort smallest to largest
	public int compare(TermFrequencyPair d1, TermFrequencyPair d2) {
		
		return (int) Math.signum(d1.getTerm() - d2.getTerm());
	}

}
