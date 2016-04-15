package searchengineGui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import searchEngine.SearchEngine;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.Checkbox;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Toolkit;

public class SearchEngineGui extends JFrame {

	private JPanel contentPane;
	private SearchEngine index = new SearchEngine();
	private JTextField QueryField;
	private JButton btnTfidf;
	
	private ArrayList<String> results;
	private JList list;
	private JButton btnCosine;
	private JButton btnBooleanand;
	private JButton btnBooleanor;
	private JButton btnLearningSearch;
	private Checkbox checkbox0;
	private Checkbox checkbox2;
	private Checkbox checkbox3;
	private JButton btnTrain;
	private Checkbox checkbox9;
	private Checkbox checkbox8;
	private Checkbox checkbox7;
	private Checkbox checkbox6;
	private Checkbox checkbox5;
	private Checkbox checkbox4;
	private Checkbox checkbox1;
	private JButton btnBm;
	private JLabel lblEnterQueryHere;
	private JLabel lblListOfRetievals;
	private JLabel lblPagerankCoefficent;
	private JLabel lblTfidfCoefficent;
	private JLabel booleanORLabel;
	private JLabel lblBooleanandCoefficent;
	private JLabel lblCosineCoefficent;
	private JButton btnPagerank;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchEngineGui frame = new SearchEngineGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SearchEngineGui() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SearchEngineGui.class.getResource("/resources/guccle.png")));
	
		view();
		loadData();
		actions();
	
	}	
	public void view(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		QueryField = new JTextField();
		QueryField.setBounds(37, 46, 271, 26);
		contentPane.add(QueryField);
		QueryField.setColumns(10);
		
		btnTfidf = new JButton("TFIDF");

		btnTfidf.setBounds(457, 46, 117, 29);
		contentPane.add(btnTfidf);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 149, 410, 215);
		contentPane.add(scrollPane);
		
		list = new JList();
		list.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		scrollPane.setViewportView(list);
		
		btnCosine = new JButton("cosine");
	
		btnCosine.setBounds(457, 78, 117, 29);
		contentPane.add(btnCosine);
		
		btnBooleanand = new JButton("booleanAND");
	
		btnBooleanand.setBounds(328, 46, 117, 29);
		contentPane.add(btnBooleanand);
		
		btnBooleanor = new JButton("TermFrequency");

		btnBooleanor.setBounds(457, 16, 117, 29);
		contentPane.add(btnBooleanor);
		
		btnLearningSearch = new JButton("LearningSearch");
	
		btnLearningSearch.setBounds(457, 347, 128, 29);
		contentPane.add(btnLearningSearch);
		
		checkbox0 = new Checkbox("");
		checkbox0.setEnabled(false);
		checkbox0.setBounds(446, 148, 128, 23);
		contentPane.add(checkbox0);
		
		checkbox1 = new Checkbox("");
		checkbox1.setEnabled(false);
		checkbox1.setBounds(446, 166, 128, 23);
		contentPane.add(checkbox1);
		
		checkbox2 = new Checkbox("");
		checkbox2.setEnabled(false);
		checkbox2.setBounds(446, 185, 128, 23);
		contentPane.add(checkbox2);
		
		checkbox3 = new Checkbox("");
		checkbox3.setEnabled(false);
		checkbox3.setBounds(446, 204, 128, 23);
		contentPane.add(checkbox3);
		
		checkbox4 = new Checkbox("");
		checkbox4.setEnabled(false);
		checkbox4.setBounds(446, 223, 128, 23);
		contentPane.add(checkbox4);
		
		checkbox5 = new Checkbox("");
		checkbox5.setEnabled(false);
		checkbox5.setBounds(446, 242, 128, 23);
		contentPane.add(checkbox5);
		
		checkbox6 = new Checkbox("");
		checkbox6.setEnabled(false);
		checkbox6.setBounds(446, 261, 128, 23);
		contentPane.add(checkbox6);
		
		checkbox7 = new Checkbox("");
		checkbox7.setEnabled(false);
		checkbox7.setBounds(446, 280, 128, 23);
		contentPane.add(checkbox7);
		
		checkbox8 = new Checkbox("");
		checkbox8.setEnabled(false);
		checkbox8.setBounds(446, 299, 128, 23);
		contentPane.add(checkbox8);
		
		checkbox9 = new Checkbox("");
		checkbox9.setEnabled(false);
		checkbox9.setBounds(446, 318, 128, 23);
		contentPane.add(checkbox9);
		
		btnTrain = new JButton("train");
		btnTrain.setEnabled(false);
		btnTrain.setBounds(467, 388, 117, 29);
		contentPane.add(btnTrain);
		
		btnBm = new JButton("bm25");

		btnBm.setBounds(325, 16, 117, 29);
		contentPane.add(btnBm);
		
		lblPagerankCoefficent = new JLabel("pageRank weight:");
		lblPagerankCoefficent.setBounds(37, 379, 167, 16);
		contentPane.add(lblPagerankCoefficent);
		
		lblTfidfCoefficent = new JLabel("TFIDF weight:");
		lblTfidfCoefficent.setBounds(37, 435, 134, 16);
		contentPane.add(lblTfidfCoefficent);
		
		booleanORLabel = new JLabel("BooleanOR weight:");
		booleanORLabel.setBounds(245, 379, 202, 16);
		contentPane.add(booleanORLabel);
		
		lblBooleanandCoefficent = new JLabel("BooleanAND weight:");
		lblBooleanandCoefficent.setBounds(245, 407, 202, 16);
		contentPane.add(lblBooleanandCoefficent);
		
		lblCosineCoefficent = new JLabel("Cosine weight:");
		lblCosineCoefficent.setBounds(37, 407, 202, 16);
		contentPane.add(lblCosineCoefficent);
		
		lblEnterQueryHere = new JLabel("ENTER QUERY HERE");
		lblEnterQueryHere.setBounds(47, 21, 157, 16);
		contentPane.add(lblEnterQueryHere);
		
		lblListOfRetievals = new JLabel("List of retreivals,  will open pages in your default browser");
		lblListOfRetievals.setBounds(37, 116, 389, 16);
		contentPane.add(lblListOfRetievals);
		
		btnPagerank = new JButton("PageRank");
	
		btnPagerank.setBounds(330, 78, 117, 29);
		contentPane.add(btnPagerank);
		
		JLabel lblSelectRelevantDocuments = new JLabel("select relevant documents");
		lblSelectRelevantDocuments.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblSelectRelevantDocuments.setBounds(446, 126, 139, 16);
		contentPane.add(lblSelectRelevantDocuments);
		
		
		
	}
	
	public void loadData(){
		index.loadData("documentVectors");
		index.loadPageRank("PageRankScore");
		index.loadWeights("weights");
		
		setLabels();
		
	}
	public void actions(){
		btnTfidf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				results = index.TF_IDF(QueryField.getText());
				list.setListData(results.toArray());
				
		
				
			}
		});
		btnCosine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.cosineSearch(QueryField.getText());
				list.setListData(results.toArray());
			}
		});
		btnBooleanand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.booleanAND(QueryField.getText());
				list.setListData(results.toArray());
			}
		});
		
		btnBooleanor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.booleanSearch(QueryField.getText());
				list.setListData(results.toArray());
				
			}
		});
		
		btnPagerank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.pagerankSearch(QueryField.getText());
				list.setListData(results.toArray());
			}
		});
		
		
		btnLearningSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.learningSearch(QueryField.getText());
				list.setListData(results.toArray());
				
				checkbox0.setEnabled(true);
				checkbox1.setEnabled(true);
				checkbox2.setEnabled(true);
				checkbox3.setEnabled(true);
				checkbox4.setEnabled(true);
				checkbox5.setEnabled(true);
				checkbox6.setEnabled(true);
				checkbox7.setEnabled(true);
				checkbox8.setEnabled(true);
				checkbox9.setEnabled(true);
				
				btnTrain.setEnabled(true);
			
	
			}
		});
		
		btnBm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				results = index.bm25search(QueryField.getText());
				list.setListData(results.toArray());
			}
		});
		
		
		
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> relevances = new ArrayList<Integer>();
			
				if (checkbox0.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox1.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox2.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox3.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox4.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox5.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox6.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox7.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox8.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				if (checkbox9.getState() == true) {
					relevances.add(1);
				}
				else {
					relevances.add(0);
				}
				//System.out.println(relevances.toString());
				index.learn(relevances);
	
				checkbox0.setEnabled(false);
				checkbox1.setEnabled(false);
				checkbox2.setEnabled(false);
				checkbox3.setEnabled(false);
				checkbox4.setEnabled(false);
				checkbox5.setEnabled(false);
				checkbox6.setEnabled(false);
				checkbox7.setEnabled(false);
				checkbox8.setEnabled(false);
				checkbox9.setEnabled(false);
				checkbox0.setState(false);
				checkbox1.setState(false);
				checkbox2.setState(false);
				checkbox3.setState(false);
				checkbox4.setState(false);
				checkbox5.setState(false);
				checkbox6.setState(false);
				checkbox7.setState(false);
				checkbox8.setState(false);
				checkbox9.setState(false);

				setLabels();
				btnTrain.setEnabled(false);
			
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
	private void setLabels(){
		lblPagerankCoefficent.setText(String.format("PageRank weight %.3f" , index.getPageRankCoefficent()));
		lblBooleanandCoefficent.setText(String.format("BooleanAND weight %.3f" , index.getbooleanANDCoefficent()));
		lblCosineCoefficent.setText(String.format("Cosine weight %.3f", index.getConsineCoeffient()));
		lblTfidfCoefficent.setText(String.format("TF_IDF weight %.3f", index.getTFIDFCoefficent()));
		booleanORLabel.setText(String.format("TF Weight %.3f", index.getCumulativeFrequencyCoefficent()));
		
	}
	

	
	/**
	 * method to link page to the web
	 * @param webAddress
	 */
	private void webLink(String webAddress){
		if(Desktop.isDesktopSupported()){
			
			Desktop desktop = Desktop.getDesktop();
			try {
				String address = "http://" + webAddress;
				URI uri = new URI(address);
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
