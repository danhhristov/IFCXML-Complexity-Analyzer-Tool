package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import core.IStorage;
import core.SimpleMetricsAnalyser;
import listeners.SelectAnalyserListener;
import listeners.SelectFileListener;
import util.FrameDetails;
import util.StatusType;
import util.UpdateType;

public class Frame implements IFrame, Observer {
	private JFrame frame;
	private JMenuBar menubar;
	private JMenu fileMenu;
	private JMenuItem selectFileMenu;
	private JMenu analysersMenu;
	private JMenuItem simpleAnalyserMenu;
	private JTabbedPane tabbedPane;
	private JPanel simpleStatsTab;
	private JPanel tableStatsTab;
	private JTable detailsTable;
	private JScrollPane tableContainer;
	private JPanel codeTab;
	private JLabel simpleStatsLabel;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private IStorage storage;

	public Frame(FrameDetails fd, IStorage s) {
		storage = s;
		storage.addObserver(this);
		initFrame(fd);
		initComponents();
		addListeners();
		addComponentsToFrame();
		frame.setVisible(true);
	}

	private void initFrame(FrameDetails fd) {
		frame = new JFrame(fd.title);
		frame.setMinimumSize(new Dimension(fd.width, fd.height));
		frame.setResizable(fd.resizable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		
		Font menuFont = new Font(Font.SANS_SERIF, 3, 18);
		
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setFont(menuFont);
		
		selectFileMenu = new JMenuItem("Select");
		selectFileMenu.setFont(menuFont);
		
		analysersMenu = new JMenu("Analysers");
		analysersMenu.setFont(menuFont);
		
		simpleAnalyserMenu = new JMenuItem("Simple");
		
		fileMenu.add(selectFileMenu);
		
		analysersMenu.add(simpleAnalyserMenu);
		
		menubar.add(fileMenu);
		menubar.add(analysersMenu);
		
		simpleStatsTab = new JPanel();
		simpleStatsLabel = new JLabel("File Stats Panel");
		simpleStatsLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		simpleStatsTab.add(simpleStatsLabel, BorderLayout.CENTER);

		statusPanel = new JPanel();
		statusPanel.setSize(680, 150);
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		
		tableStatsTab = new JPanel();
		
		String[] detailsTableColumns = {"Node Name", "Count"};
		Object[][] tableData = {{"",""}};
		
		detailsTable = new JTable(tableData ,detailsTableColumns);
		detailsTable.setEnabled(false); //A cheat to prevent users from editing the table data.
		tableContainer = new JScrollPane(detailsTable);
		detailsTable.setFillsViewportHeight(true);
		tableStatsTab.add(tableContainer);
		
		codeTab = new JPanel();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(Font.SANS_SERIF, 1, 24));
		tabbedPane.addTab("Simple Stats", simpleStatsTab);
		tabbedPane.addTab("Detailed Stats", tableStatsTab);
		tabbedPane.addTab("Code", codeTab);
		
	}

	private void addListeners() {
		selectFileMenu.addActionListener(new SelectFileListener(this, storage));
		simpleAnalyserMenu.addActionListener(new SelectAnalyserListener(storage, new SimpleMetricsAnalyser()));
	}

	private void addComponentsToFrame() {
		frame.getContentPane().add(tabbedPane);
		frame.getContentPane().add(statusPanel, BorderLayout.PAGE_END);
		frame.setJMenuBar(menubar);
	}

	@Override
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	@Override
	public Component[] getComponents() {
		return frame.getContentPane().getComponents();
	}


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
	
	private void addStats(String stats){
		simpleStatsLabel.setText(stats);
	}

	@Override
	public Component getFrame() {
		return frame;
	}

	@Override
	public void update(Observable o, Object arg) {
		int update = storage.getUpdate();
		if (update == UpdateType.STATUS) {
			setStatusText(storage.getStatus(), storage.getStatusType());
		} else if (update == UpdateType.STATISTICS) {
			setStatusText(storage.getStatus(), storage.getStatusType());
			addStats(storage.getFileStatistics());
		}

	}

}
