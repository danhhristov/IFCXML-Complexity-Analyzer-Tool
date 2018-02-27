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

public class Frame implements IFrame, Observer {
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel simpleStatsTab;
	private JButton loadFileButton;
	private JPanel tableStatsTab;
	private JButton advancedStatsButton;
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
		lockFrame();
	}

	private void initFrame(FrameDetails fd) {
		frame = new JFrame(fd.title);
		frame.setMinimumSize(new Dimension(fd.width, fd.height));
		frame.setResizable(fd.resizable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		
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

		statusPanel = new JPanel();
		statusPanel.setSize(680, 150);
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		
		
		tableStatsTab = new JPanel();
		advancedStatsButton = new JButton("Generate Advanced Stats");
		advancedStatsButton.addActionListener(new GenerateAdvancedAnalysisListener(this.storage));
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
		
		codeTab = new JPanel();
		
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font(Font.SANS_SERIF, 1, 24));
		tabbedPane.addTab("Simple Stats", simpleStatsTab);
		tabbedPane.addTab("Detailed Stats", tableStatsTab);
		tabbedPane.addTab("Code", codeTab);
		
	}

	private void addListeners() {
		loadFileButton.addActionListener(new SelectFileListener(this, storage));
	}

	private void addComponentsToFrame() {
		frame.getContentPane().add(tabbedPane);
		frame.getContentPane().add(statusPanel, BorderLayout.PAGE_END);
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
		
		if(storage.getStatusType() == StatusType.NORMAL){
			lockFrame();
		} else {
			unlockFrame();
		}
		
		if (update == UpdateType.STATUS) {
			setStatusText(storage.getStatus(), storage.getStatusType());
		} else if (update == UpdateType.SIMPLE_STATISTICS) {
			setStatusText(storage.getStatus(), storage.getStatusType());
			addStats(storage.getFileStatistics());
		} else if (update == UpdateType.TABLE_STATISTICS){
			setStatusText(storage.getStatus(), storage.getStatusType());
		}

	}
	
	@Override
	public void lockFrame() {
		advancedStatsButton.setEnabled(false);
		//JTabbedPanes cannot be disabled in the same way buttons can...
		//Need to find different solution for those.
	}

	@Override
	public void unlockFrame() {
		advancedStatsButton.setEnabled(true);
	}

}
