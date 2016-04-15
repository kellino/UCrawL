package pageRank;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Class for representing a collection of webpages 
 * as a graph, 
 * this can be used for link analysis ie pagerank.
 * 
 * adjacency matrix represents a graph an a hashset of its connections.
 * 
 * @author jncys
 *
 */
public class PageRank {

	int pageCounter = 0;
	// HashSet to see if a page has been processed yet.
	HashSet<String> processed = new HashSet<String>();

	// nested arraylist of pages with their corresponding links.
	ArrayList<HashSet<Integer>> matrix = new ArrayList<HashSet<Integer>>();

	// array saving page URLs in an array,
	ArrayList<String> idToPage = new ArrayList<String>();

	// map so we can quickly look up page IDs from the URLs.
	HashMap<String, Integer> pageID = new HashMap<String, Integer>();

	/**
	 * Add pages and their links to the matrix
	 * 
	 * @param page
	 * @param links
	 */
	public void addPage(String page, String[] links) {

		// if a page has already been processed, ignore
		// theoretically pages which have been processed already will not be
		// submitted.
		if (!processed.contains(page)) {
			processed.add(page);

			// check if a page already has an ID number, may already
			// have been assigned a number if if is a link.
			// increment the number of pages in the graph.
			if (!pageID.containsKey(page)) {
				pageID.put(page, pageCounter);

				idToPage.add(page);
				matrix.add(new HashSet<Integer>());
				pageCounter++;

			}
			// check the links from the page.
			HashSet<Integer> linkslist = new HashSet<Integer>();

			for (int i = 0; i < links.length; i++) {
				if (!pageID.containsKey(links[i])) {
					pageID.put(links[i], pageCounter);
					idToPage.add(links[i]);
					matrix.add(new HashSet<Integer>());
					linkslist.add(pageCounter);
					pageCounter++;
				} else {
					linkslist.add(pageID.get(links[i]));
				}
			}
			matrix.set(pageID.get(page), linkslist);
			

		}


	}

	/**
	 * calculate the page rank
	 * 
	 * @param teleporting
	 *            change of teleporting. usually set as 0.15
	 * @throws IOException 
	 */
	public void pageRank(double teleporting) throws IOException {
		System.out.println("pages in graph = " + matrix.size());
		double[] output = new double[matrix.size()];
		double[] probabilities = new double[matrix.size()];
		for (int i = 0; i < output.length; i++) {
			output[i] = 1.0 / matrix.size();
		}

		// this should stay the same all of the time
		double teleportingweight = teleporting / matrix.size();

		// double[] probabilties = new double[matrix.size()];
		// iterations of the power
		for (int s = 0; s < 20; s++) {
			boolean stable =false;
			
			//check for stability
			for (int i = 0; i < probabilities.length; i++) {
				if( Math.abs(probabilities[i]-output[i])<0.000001){
					stable = true;		
				}	
				else{
					stable = false;
					break;
				}
			}
			
			System.out.println("iteration " + s);
			if(stable){
				break;
			}
			probabilities = output;
			output = new double[matrix.size()];

			for (int i = 0; i < probabilities.length; i++) {

				
				
				
				
				//if no links then then even chance of teleporting to any webpage
				if (matrix.get(i).size() == 0) {
					for (int j = 0; j < probabilities.length; j++) {
						output[j] += (probabilities[i] / matrix.size());
					}
					
				}

				else {
					//int counter = 0;
					double linkweight = (1.0 - teleporting) / matrix.get(i).size();
					
					for (int j = 0; j < output.length; j++) {

						
						if (matrix.get(i).contains(j)) {
						//if(contained == true)	{
							output[j] += (probabilities[i] * (teleportingweight + linkweight));
							//counter++;
						} else {
							output[j] += (probabilities[i] * teleportingweight);
						}
					}
				}
			}
		}
		
		ArrayList<Webpage> ranking = new ArrayList<Webpage>();
		
		for (int i = 0; i < matrix.size(); i++) {
			
			ranking.add(new Webpage(idToPage.get(i),output[i]));
			
		}
		Collections.sort(ranking,new WebpageComparator());
		File file = new File("PageRankScore");
		FileWriter fw = new FileWriter(file,false);
		for (Webpage webpage : ranking) {
			fw.append(webpage.getName() + " ");
			double probability = webpage.getVisitProbability();//* matrix.size();
			fw.append(probability + "\n");
			
			
			
		}
		fw.close();
		
		System.out.println(matrix.size());
		for (int i = 0; i < 30; i++) {
			System.out.println(ranking.get(i).toString());
			
		}

	}

}
