package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;

//import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import core.IStorage;
import core.SimpleMetricsAnalyser;
import listeners.AnalyseFileListener;
import listeners.OpenFileListener;
import listeners.SelectAnalyserListener;
import util.FrameDetails;
import util.StatusType;
import util.UpdateType;

import java.util.Observer;

public class Frame implements IFrame, Observer {
	private JFrame frame;
	private JMenuBar menubar;
	private JMenu fileMenu;
	private JMenuItem openFileMenu;
	private JMenu runMenu;
	private JMenuItem analyseMenu;
	private JMenu analysersMenu;
	private JMenuItem simpleAnalyserMenu;
	private JPanel statsPanel;
	private JLabel statsLabel;
	private JPanel navigationPanel;
	private JButton prevB;
	private JButton nextB;
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
		frame.setSize(new Dimension(fd.width, fd.height));
		frame.setResizable(fd.resizable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		Font menuFont = new Font(Font.SANS_SERIF, 3, 18);
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		fileMenu.setFont(menuFont);
		openFileMenu = new JMenuItem("Open...");
		openFileMenu.setFont(menuFont);
		analysersMenu = new JMenu("Analysers");
		analysersMenu.setFont(menuFont);
		simpleAnalyserMenu = new JMenuItem("Simple");

		runMenu = new JMenu("Run");
		runMenu.setFont(menuFont);
		analyseMenu = new JMenuItem("Analyse");
		analyseMenu.setFont(menuFont);

		statsPanel = new JPanel();
		statsPanel.setSize(680, 500);

		statsLabel = new JLabel("File Stats Panel");
		statsLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));
		statsPanel.add(statsLabel, BorderLayout.CENTER);

		navigationPanel = new JPanel();
		navigationPanel.setSize(680, 150);

		Font bFont = new Font(Font.SANS_SERIF, 1, 32);
		prevB = new JButton("Previous");
		nextB = new JButton("Next");
		prevB.setFocusPainted(false);
		nextB.setFocusPainted(false);
		prevB.setFont(bFont);
		nextB.setFont(bFont);

		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));

		statusPanel = new JPanel();
		statusPanel.add(statusLabel);
		statusPanel.setToolTipText("Status");
		statusPanel.setForeground(Color.YELLOW);
		statusPanel.setSize(new Dimension(100, 250));
		statusPanel.setMinimumSize(new Dimension(100, 250));

		navigationPanel.add(prevB, BorderLayout.LINE_START);
		navigationPanel.add(statusPanel, BorderLayout.CENTER);
		navigationPanel.add(nextB, BorderLayout.LINE_END);

		fileMenu.add(openFileMenu);
		runMenu.add(analyseMenu);
		analysersMenu.add(simpleAnalyserMenu);
		menubar.add(fileMenu);
		menubar.add(runMenu);
		menubar.add(analysersMenu);
	}

	private void addListeners() {
		// RUN menu should be shown only if both file and analyzer are selected.
		openFileMenu.addActionListener(new OpenFileListener(this, storage));
		analyseMenu.addActionListener(new AnalyseFileListener(storage));
		simpleAnalyserMenu.addActionListener(new SelectAnalyserListener(storage, new SimpleMetricsAnalyser()));
	}

	private void addComponentsToFrame() {
		frame.getContentPane().add(statsPanel, BorderLayout.CENTER);
		frame.getContentPane().add(navigationPanel, BorderLayout.PAGE_END);
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
		if (statusType == StatusType.ERROR) {
			c = Color.RED;
		} else if (statusType == StatusType.NORMAL) {
			c = Color.YELLOW;
		} else if (statusType == StatusType.SUCCESS) {
			c = Color.GREEN;
		}
		statusLabel.setText(txt);
		statusLabel.setForeground(c);
	}
	
	private void addStats(String stats){
		statsLabel.setText(stats);
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
