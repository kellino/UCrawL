package comparators;

import java.util.Comparator;

import searchEngine.RetrievedDocument;

public class PageRankComparator implements Comparator<RetrievedDocument>{
	@Override
	public int compare(RetrievedDocument o1, RetrievedDocument o2) {
		
		return (int) Math.signum(o2.getPageRankScore() - o1.getPageRankScore());
		
	}
}
