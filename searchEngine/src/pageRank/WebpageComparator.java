package pageRank;

import java.util.Comparator;

/**
 * order webpages by their visit probability ie PageRank
 * @author jncys
 *
 */
public class WebpageComparator implements Comparator<Webpage>{

	@Override
	public int compare(Webpage o1, Webpage o2) {
		
		return (int) Math.signum(o2.getVisitProbability() - o1.getVisitProbability());
	}
}
