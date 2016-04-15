package comparators;

import java.util.Comparator;

import searchEngine.RetrievedDocument;

public class BM25Comparator implements Comparator<RetrievedDocument>{
	
	public int compare(RetrievedDocument o1, RetrievedDocument o2) {
		
		return (int) Math.signum(o2.getBm25Score() - o1.getBm25Score());		
	}
}
