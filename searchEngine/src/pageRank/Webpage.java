package pageRank;

/**
 * object to hold data relating to webpages when ranking by pagerank
 * @author jncys
 *
 */
public class Webpage {

	private String name;
	private double visitProbability;

	public Webpage(String name, double visitProbability) {
		this.name = name;
		this.visitProbability = visitProbability;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVisitProbability() {
		return visitProbability;
	}

	public void setVisitProbability(double visitProbability) {
		this.visitProbability = visitProbability;
	}

	@Override
	public String toString() {
		return "Webpage [name = " + name + ", visitProbability=" + visitProbability + "]";
	}
	


}
