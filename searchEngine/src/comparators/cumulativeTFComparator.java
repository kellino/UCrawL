package comparators;

import java.util.Comparator;

import searchEngine.RetrievedDocument;

public class cumulativeTFComparator implements Comparator<RetrievedDocument> {
	public int compare(RetrievedDocument o1, RetrievedDocument o2) {
		
		return (int) Math.signum(o2.getCumulativetermFrequency() - o1.getCumulativetermFrequency());
		
	}


}
