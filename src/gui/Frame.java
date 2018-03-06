package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import core.IStorage;
import listeners.GenerateAdvancedAnalysisListener;
import listeners.SelectFileListener;
import util.FrameDetails;
import util.StatusType;
import util.UpdateType;
/**
 * The Frame class is the main GUI component of this project.
 * It holds all of the other components which are used to build up the User Interface.
 * 
 * @author Daniel Hristov (2018)
 *
 */
public class Frame implements IFrame, Observer {
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel simpleStatsTab;
	private JButton loadFileButton;
	private JPanel tableStatsTab;
	private JButton advancedStatsButton;
	private JTable detailsTable;
	private JScrollPane tableContainer;
	private JScrollPane helpScrollPane;
	private JLabel helpLabel;
	private JPanel helpTab;
	private JLabel simpleStatsLabel;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private IStorage storage;
	private String helpText = "<html><h1 style='width: 750px; text-align: center'>Welcome!</h1>"
			+ "<p style='width: 570px; font-size: 1.15em'>This tool's aim is to help you understand interesting features about a given project."
			+ "<br>To start, please go to the 'Simple Stats' tab and click the 'Select a file' button."
			+ "<br>Navigate to the desired Ifc2x3 XML file and select OK."
			+ "<br>Once the file has loaded, you should see some simple statistics about the selected file."
			+ "<br>To see more detailed statistics and information about the file, please navigate to the 'Detailed Stats' tab."
			+ "<br>Once you have a file loaded in, you can click the button on the bottom to generate the detailed statistics."
			+ "<br>If you wish to select another file for analysis, just head back to the 'Simple Stats' tab and load the new file."
			+ "<br>For further help and full details about each metric please refer to the user manual.</p>"
			+ "<br><br><br><p style='width: 750px; text-align: center;'><b><i>Created by Daniel Hristov</i></b></p></html>";

	public Frame(FrameDetails fd, IStorage s) {
		storage = s;
		storage.addObserver(this);
		initFrame(fd);
		initComponents();
		addListeners();
		addComponentsToFrame();
		frame.setVisible(true);
		lockFrame();
	}

	/**
	 * Initialise the frame with the provided FrameDetails object
	 * @param fd: FrameDetails object which holds the title, width, height and resizable(boolean) properties
	 * to be set for the given frame
	 */
	private void initFrame(FrameDetails fd) {
		frame = new JFrame(fd.title);
		frame.setMinimumSize(new Dimension(fd.width, fd.height));
		frame.setResizable(fd.resizable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Initialise all components of the Frame (Labels, Buttons, Panels, etc.)
	 */
	private void initComponents() {
		/* First Tab - Simple Stats */
		loadFileButton = new JButton("Select a file");
		loadFileButton.setFont(new Font(Font.SANS_SERIF, 3, 34));
		loadFileButton.setPreferredSize(new Dimension(300, 80));
		simpleStatsTab = new JPanel();
		simpleStatsLabel = new JLabel("File Stats Panel");
		simpleStatsLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		simpleStatsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		simpleStatsLabel.setVerticalAlignment(SwingConstants.CENTER);
		simpleStatsLabel.setPreferredSize(new Dimension(690, 470));
		simpleStatsTab.add(simpleStatsLabel, BorderLayout.LINE_START);
		simpleStatsTab.add(loadFileButton, BorderLayout.CENTER);
		/* End of First Tab - Simple Stats */
		
		/* Status Bar */
		statusPanel = new JPanel();
		statusPanel.setSize(680, 150);
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		/* End of Status Bar */
		
		/* Second Tab - Detailed Stats / Table Stats */
		tableStatsTab = new JPanel();
		advancedStatsButton = new JButton("Generate Advanced Stats");
		advancedStatsButton.setPreferredSize(new Dimension(700,50));
		advancedStatsButton.setFont(new Font(Font.SANS_SERIF, 1, 16));
		detailsTable = new JTable(storage.getTableModel());
		detailsTable.setFont(new Font(Font.SANS_SERIF, 0, 16));
		detailsTable.setRowHeight(26);
		tableContainer = new JScrollPane(detailsTable);
		detailsTable.setFillsViewportHeight(true);
		detailsTable.setPreferredScrollableViewportSize(new Dimension(700,450));
		tableStatsTab.add(tableContainer);
		tableStatsTab.add(advancedStatsButton);
		/* End of Second Tab - Detailed Stats / Table Stats */
		
		/* Third Tab - Help */
		helpTab = new JPanel();
		helpLabel = new JLabel(helpText);
		helpLabel.setFont(new Font(Font.SANS_SERIF, 1, 16));
		helpScrollPane = new JScrollPane(helpLabel);
		helpTab.add(helpScrollPane);
		/* End of Third Tab - Help */
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(Font.SANS_SERIF, 1, 24));
		tabbedPane.addTab("Simple Stats", simpleStatsTab);
		tabbedPane.addTab("Detailed Stats", tableStatsTab);
		tabbedPane.addTab("Help", helpTab);
	}

	/**
	 * Associate all buttons with the correct listeners
	 */
	private void addListeners() {
		loadFileButton.addActionListener(new SelectFileListener(this, storage));
		advancedStatsButton.addActionListener(new GenerateAdvancedAnalysisListener(this.storage));
	}

	/**
	 * Add all components to the frame
	 */
	private void addComponentsToFrame() {
		frame.getContentPane().add(tabbedPane);
		frame.getContentPane().add(statusPanel, BorderLayout.PAGE_END);
	}

	/**
	 * Sets the visible property of the frame
	 * @param b: boolean
	 */
	@Override
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	
	/**
	 * Get all components of the frame
	 */
	@Override
	public Component[] getComponents() {
		return frame.getContentPane().getComponents();
	}
	
	/**
	 * Sets the text in the status bar to the String txt.
	 * @param txt: String to be displayed in the Status Bar
	 * @param statusType: Integer to represent the type of status to be displayed: 
	 * StatusType.SUCCESS displays the text in Green, StatusType.NORMAL in yellow
	 * and StatusType.ERROR in red
	 */
	private void setStatusText(String txt, int statusType) {
		Color c = null;
		frame.getContentPane().setCursor(Cursor.getDefaultCursor());
		if (statusType == StatusType.ERROR) {
			c = Color.RED;
		} else if (statusType == StatusType.NORMAL) {
			c = Color.YELLOW;
			frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else if (statusType == StatusType.SUCCESS) {
			c = Color.GREEN;
		}
		statusLabel.setText(txt);
		statusLabel.setForeground(c);
	}
	
	/**
	 * Sets the text of the simpleStatsLabel to the passed String.
	 * @param stats: String to be displayed in the Simple Stats panel.
	 */
	private void addStats(String stats){
		simpleStatsLabel.setText(stats);
	}

	@Override
	public Component getFrame() {
		return frame;
	}

	/**
	 * A customized version of the Observer pattern 'update' method.
	 * This method gets the update status from the observables it observers
	 * and decides how to proceed
	 * @requires The observable this frame is observing to provide a method
	 * which returns an Integer. e.g. UpdateType.SIMPLE_STATISTICS
	 */
	@Override
	public void update(Observable o, Object arg) {
		int update = storage.getUpdate();
		
		if(storage.getStatusType() == StatusType.NORMAL){
			lockFrame();
		} else {
			unlockFrame();
		}
		
		//For each update there is a new status, so update the Status Bar with the latest one.
		setStatusText(storage.getStatus(), storage.getStatusType());

		
		if (update == UpdateType.SIMPLE_STATISTICS) {
			addStats(storage.getFileStatistics());
		} else {
			//update == UpdateType.STATUS || update == UpdateType.TABLE_STATISTICS
			//In both cases the Frame is not required to do anything which it hasn't already done, so we just ignore the update
		}

	}
	
	/**
	 * Disable all functionality that requires a file to be loaded before it can be used.
	 */
	@Override
	public void lockFrame() {
		advancedStatsButton.setEnabled(false);
	}

	/**
	 * Enable all functionality that requires a file to be loaded before it can be used.
	 */
	@Override
	public void unlockFrame() {
		advancedStatsButton.setEnabled(true);
	}

}
