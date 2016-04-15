package comparators;

import java.util.Comparator;

import searchEngine.RetrievedDocument;

public class RetrievedDocumentComparator implements Comparator<RetrievedDocument>{

	public int compare(RetrievedDocument d1, RetrievedDocument d2) {
		
		return (int) Math.signum(d2.getRating() - d1.getRating());
	}
}