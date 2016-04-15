package pageRank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class PageRankStart {

	public static void main(String[] args) throws IOException {
		PageRank lg = new PageRank();


		File file = new File("longer_crawl_without_http");
		Scanner scanner = new Scanner(file);
		int count = 0;
		while (scanner.hasNextLine()) {
			try {
				String[] line = scanner.nextLine().split(" = ");
				String pagename = line[0].trim();
				
				String[] linksList;
				if (line[1].equals("[]")) {
					linksList = new String[0];
				}

				else {
					String links = line[1].replace("[", "");
					links = links.replace("]", "");
					linksList = links.split(", ");
				}
				count++;
				System.out.println(count);
				lg.addPage(pagename, linksList);

			} catch (Exception e) {
				System.err.println("incorrect string format");
			}

		}

		System.out.println("pages added");
		lg.pageRank(0.15);

	}

}
