package oldSearchEngineGui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import searchEngine.PostingListIndex;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class OldSearchEngine extends JFrame {

	private JPanel contentPane;
	private JTextField searchField;
	private JButton SearchTFIDF;
	private JButton btnSearch;
	private PostingListIndex index;
	private JList list;
	
	
	
	private ArrayList<String> results;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OldSearchEngine frame = new OldSearchEngine();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws FileNotFoundException 
	 */
	public OldSearchEngine() throws FileNotFoundException {

		view();
		initalise();
		actions();
	}

	private void initalise() throws FileNotFoundException {
		index = new PostingListIndex();
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
		
		System.out.println(index.getVocabularySize());
	}

	private void view() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		searchField = new JTextField();
		searchField.setBounds(22, 39, 251, 34);
		contentPane.add(searchField);
		searchField.setColumns(10);

		btnSearch = new JButton("Boolean TF");
		
		btnSearch.setBounds(292, 18, 117, 29);
		contentPane.add(btnSearch);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 100, 394, 158);
		contentPane.add(scrollPane);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		scrollPane.setViewportView(list);

		SearchTFIDF = new JButton("TF-IDF");

		SearchTFIDF.setBounds(292, 59, 117, 29);
		contentPane.add(SearchTFIDF);
		
		JLabel lblEnterQueryHere = new JLabel("Enter Query Here");
		lblEnterQueryHere.setBounds(22, 23, 162, 16);
		contentPane.add(lblEnterQueryHere);
	}

	private void actions() {
		SearchTFIDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query = searchField.getText();
				results =index.TF_IDF(query);
				list.setListData(results.toArray());

			}
		});
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query = searchField.getText();
				results = index.booleanAND(query);
				list.setListData(results.toArray());

				
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				webLink(results.get(list.getSelectedIndex()));
				list.getSelectedIndex();
			}
		});
		
		
	}
	
	private void webLink(String webAddress){
		if(Desktop.isDesktopSupported()){
			
			Desktop desktop = Desktop.getDesktop();
//			String removeSpaces = txtAilment.getText().replace(" ", "_");
//			String webAddress = String.format("https://www.wikipedia.org/wiki/%s", removeSpaces);
//			webAddress.replaceAll(" ", "");
			try {
				URI uri = new URI(webAddress);
				desktop.browse(uri);
			} catch (URISyntaxException e1) {
				System.out.println("URI exception in websearch");
				e1.printStackTrace();
			} catch (IOException e1) {
				System.out.println("IO exception in websearch");
				e1.printStackTrace();
			}
		}
		
		
	}
}
