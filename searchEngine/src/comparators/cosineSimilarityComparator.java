package comparators;

import java.util.Comparator;

import searchEngine.RetrievedDocument;

public class cosineSimilarityComparator implements Comparator<RetrievedDocument>{
	public int compare(RetrievedDocument o1, RetrievedDocument o2) {
		
		return (int) Math.signum(o2.getCosineScore() - o1.getCosineScore());		
	}
}
