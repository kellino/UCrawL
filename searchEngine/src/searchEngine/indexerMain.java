
package searchEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class indexerMain {

	public static void main(String[] args) throws FileNotFoundException {

PostingListIndex index = new PostingListIndex();
		
		File file = new File("ucl_clean");
		int count = 0;
		Scanner scanner = new Scanner(file);
		StringBuilder documentBody;
		while(scanner.hasNextLine()){
			scanner.nextLine();
			String documentName = scanner.nextLine();
			documentBody = new StringBuilder();
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				if(line.equals("")){
					index.addDocument(documentName, documentBody.toString());
					//System.out.println(documentBody.toString());
					break;
				}
				documentBody.append(line);
			}
		}
		scanner.close();
		System.out.println(index.documentCount);
		System.out.println(index.getVocabularySize());
		//index.TF_IDF("elephants");
		index.TF_IDF("");















//		PostingListIndex index= new PostingListIndex();
//	
//		File documents = new File("collection.spec");
//
//		Scanner scanner1 = new Scanner(documents);
//		
//		for (int i = 0; i < 1; i++) {
//			System.out.println(i);
//			
//			String documentName = scanner1.nextLine();
//			StringBuilder documentBody = new StringBuilder();
//			
//			File file = new File(documentName);
//			
//			Scanner scanner2 = new Scanner(file);;
//		
//			while(scanner2.hasNextLine()){
//				documentBody.append(scanner2.nextLine());
//			
//			}
//
//			index.addDocument(documentName, documentBody.toString());
//		
//			scanner2.close();
//		}
//		scanner1.close();
//		System.out.println(index.documentCount);
//		
//		System.out.println(index.getVocabularySize());
//		
//		
//		index.TF_IDF("raspberry pi");
//		
//		
	}


}
