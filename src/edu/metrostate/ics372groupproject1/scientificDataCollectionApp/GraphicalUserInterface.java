package edu.metrostate.ics372groupproject1.scientificDataCollectionApp;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

public class GraphicalUserInterface {

	private JFrame frame;
	private JTextField textField;
	private File JSONFile = null;
	private String siteID;
	private IOInterface myInterface;
	private Site selectedSite;
	private ArrayList<Site> listOfSite = new ArrayList<>();

	//Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicalUserInterface window = new GraphicalUserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Create the application (the constructor).
	public GraphicalUserInterface() {
		//call to instance method in the constructor
		initialize();
	}

	//Initialize the contents of the frame.
	private void initialize() {
		frame = new JFrame("Scientific Data Recorder");
		frame.setFont(new Font("Tahoma", Font.BOLD, 13));
		frame.setBounds(100, 100, 599, 723);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JTextArea textArea = new JTextArea();
		JTextArea display = new JTextArea();
		JTextArea statusField = new JTextArea();
		myInterface = new IOInterface(frame);
		
		JLabel header = new JLabel("To start recording , choose a JSON file to be read...");
		header.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		header.setBounds(147, 11, 272, 25);
		frame.getContentPane().add(header);
		
		//Choose the file to be read(JSON)
		JButton UploadButton = new JButton("Upload JSON");
		UploadButton.setToolTipText("Navigate to the JSON file.");
		UploadButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		UploadButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		UploadButton.setBounds(40, 74, 114, 23);
		frame.getContentPane().add(UploadButton);
		UploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JSONFile = myInterface.chooseFile();
					textArea.setText(myInterface.getFileName());
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(frame, e.getStackTrace());
				}
			}
		});
		
		textArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma", Font.ITALIC, 13));
		textArea.setBounds(190, 74, 229, 22);
		frame.getContentPane().add(textArea);
		
		
		//This functional button will call the ReadJson() with the input JSON as parameter
		JButton readButton = new JButton("Read File");
		readButton.setToolTipText("Read the selected JSON file.");
		readButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		readButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		readButton.setBounds(40, 120, 114, 23);
		frame.getContentPane().add(readButton);
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//Call to the method to read the JSON
				if(JSONFile != null) {
					try {
						myInterface.ReadJson(JSONFile);
						//Display the content of the input JSON
						display.setText(display.getText() +"\n"+ myInterface.getListOfSite());
					}catch(Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Error Reading The File!");
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Please choose a File to be read!");
				}
			}
		});
		
		JLabel siteId = new JLabel("Site ID:");
		siteId.setFont(new Font("Tahoma", Font.BOLD, 12));
		siteId.setBounds(40, 171, 63, 25);
		frame.getContentPane().add(siteId);
		
		//Text field that takes the site ID of selected site
		textField = new JTextField();
		textField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textField.setBounds(190, 172, 151, 25);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				siteID = textField.getText();
				//create a new site to contain the collection
				selectedSite = new Site();
				//Display the siteID to the user
				textField.setText("");
				statusField.setText("locationID: "+siteID);
			}
		});
		
		//this functional button allow a site collection to start saving
		JButton startButton = new JButton("Start Collection");
		startButton.setToolTipText("Start site collection.");
		startButton.setBackground(UIManager.getColor("Button.background"));
		startButton.setForeground(Color.BLACK);
		startButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		startButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		startButton.setBounds(313, 220, 114, 23);
		frame.getContentPane().add(startButton);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(siteID != null) {
					selectedSite.setRecording(true);
					//Show the selected site and status in the text field
					String info = "locationID: "+siteID +" is now Collecting.";
					statusField.setText(info);
				}
				else {
					JOptionPane.showMessageDialog(frame, "Please Enter a site ID to start collecting!");
				}
			}
		});
		
		JButton EndButton = new JButton("End Collection");
		EndButton.setToolTipText("End site collection");
		EndButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		EndButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		EndButton.setBackground(UIManager.getColor("Button.background"));
		EndButton.setBounds(432, 220, 114, 23);
		frame.getContentPane().add(EndButton);
		EndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(siteID != null) {
					//This is where the call to the method to stop saving will go
					selectedSite.setRecording(false);
					//Show the selected site and status in the text field
					String info = "locationID: "+siteID +" is no longer Collecting.";
					statusField.setText(info);
				}
				else {
					JOptionPane.showMessageDialog(frame, "Please Enter a site ID to stop collecting from!");
				}
			}
		});
		statusField.setEditable(false);
		statusField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		statusField.setBounds(40, 219, 258, 23);
		frame.getContentPane().add(statusField);
		
		
		//this Text area will contain the site reading 
		display.setEditable(false);
		display.setLineWrap(true);
		display.setBorder(new TitledBorder ( new EtchedBorder (), "Site Reading"));
		JScrollPane scrollPane = new JScrollPane(display);
		scrollPane.setBounds(40, 303, 506, 313);
		scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		frame.getContentPane().add(scrollPane);

		//Add collection to a specified site
		JButton addButton = new JButton("Add Collection");
		addButton.setToolTipText("Add Items to Site.");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (siteID != null) {
					//referenced the selected site matching the site ID
					myInterface.getSite(siteID, selectedSite);
					listOfSite.add(selectedSite);
				}
				else {
					JOptionPane.showMessageDialog(frame, "Please enter a site to add collection to!");
				}
			}
		});
		addButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		addButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		addButton.setBounds(39, 269, 115, 23);
		frame.getContentPane().add(addButton);
		
		//View the site collections
		JButton viewButton = new JButton("View Reading");
		viewButton.setToolTipText("Show Items for a Site.");
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (siteID != null) {
					//display a selected site's reading
					display.setText(selectedSite.toString());
				}
				else {
					JOptionPane.showMessageDialog(frame, "Nothing to display!");
				}
			}
		});
		viewButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		viewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		viewButton.setBounds(40, 638, 114, 23);
		frame.getContentPane().add(viewButton);
		
		
		//this functional button will export the site collection in a JSON format
		JButton exportButton = new JButton("Export JSON");
		exportButton.setToolTipText("Export your site(s) to a JSON file.");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//This is where the call to the method to write the JSON to a file will go
				try {
					String outputFileName = JOptionPane.showInputDialog(frame,"Name your Export File...");
					if(outputFileName.equals("")) {
						outputFileName = "SiteRecord";
					}
					myInterface.writeToFile(listOfSite, outputFileName);
				}catch(Exception e) {
					JOptionPane.showMessageDialog(frame, "Export Cancelled!");
				}
			}
		});
		exportButton.setAutoscrolls(true);
		exportButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		exportButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, Color.DARK_GRAY, null));
		exportButton.setBounds(432, 638, 114, 23);
		frame.getContentPane().add(exportButton);
	}
}
