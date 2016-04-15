package searchEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class writeDocumentVectorFile{
	
	
	
	public static void createFile(String dataName){
		File datafile = new File(dataName);
		File destinationfile = new File("documentVectors");
		
		try {
			FileWriter fw = new FileWriter(destinationfile);
			Scanner scanner = new Scanner(datafile);
			
			String documentName = "";
			
			while(scanner.hasNextLine()){
				scanner.nextLine();
				String line1  = "";
				
				try{
					line1 = scanner.nextLine();
					documentName =line1.split("//")[1];
				}
				catch(Exception e){
					System.out.println(line1);
				}
				//hashmap to collect the terms and frequencies
				HashMap<String, Integer> termFrequencies = new HashMap<String, Integer>();
				//HashMap<String, Integer> firstLineFrequencies = new HashMap<String, Integer>();
				
				//check what is in the fist line.
//				String fstline = scanner.nextLine();
//				String[] fstlinearray = fstline.toLowerCase().split("[^a-z]+");
//				for (String string : fstlinearray) {
//					string = Stemmer.stem(string);
//					if(!firstLineFrequencies.containsKey(string)){
//						firstLineFrequencies.put(string, 1);
//					}else{
//						firstLineFrequencies.replace(string, firstLineFrequencies.get(string)+1);
//					}
//					if(!termFrequencies.containsKey(string)){
//						termFrequencies.put(string, 1);
//					}else{
//						termFrequencies.replace(string, termFrequencies.get(string)+1);
//					}
//				}
//				//append the first line vector to the output file.
//				fw.append(documentName);
//				for (String string : termFrequencies.keySet()) {
//					if(string !=null && !string.equals("")){
//						fw.append(" "+string + ":::"+ termFrequencies.get(string));	
//					}
//				}
//				
//				fw.append("\n");
		
				//now check what is in the rest of the document
				while(scanner.hasNextLine()){
					String line = scanner.nextLine();
					if(!line.equals("")){
						String[] lineArray = line.toLowerCase().split("[^a-z]+");
						for (String string : lineArray) {
							string = Stemmer.stem(string);
							if(!termFrequencies.containsKey(string)){
								termFrequencies.put(string, 1);
							}else{
								termFrequencies.replace(string, termFrequencies.get(string)+1);
							}
						}
	
					}
					else{
						//append the whole document vector to the output file
						fw.append(documentName);
						for (String string : termFrequencies.keySet()) {
							if(string !=null && !string.equals("")){
								fw.append(" "+string + ":::"+ termFrequencies.get(string));	
							}
						}
						
						fw.append("\n");
						
					
						break;
						
					}
				}	
			}
			fw.close();
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
