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
	private JPanel highlightsPanel;
	private JLabel highlightsLabel;
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
//		frame.pack();
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

		highlightsPanel = new JPanel();
		highlightsPanel.setSize(680, 500);
		// highlightsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));

		highlightsLabel = new JLabel("Highlights Panel");
		highlightsLabel.setFont(new Font(Font.SANS_SERIF, 1, 70));
		highlightsPanel.add(highlightsLabel, BorderLayout.CENTER);

		navigationPanel = new JPanel();
		navigationPanel.setSize(680, 150);
		// navigationPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

//		Dimension bDimensions = new Dimension();
		Font bFont = new Font(Font.SANS_SERIF, 1, 32);
//		Color bColor = Color.GREEN;
		prevB = new JButton("Previous");
		nextB = new JButton("Next");
//		prevB.setSize(150, 300);
//		nextB.setSize(150, 300);
		prevB.setFocusPainted(false);
		nextB.setFocusPainted(false);
		prevB.setFont(bFont);
		nextB.setFont(bFont);
		// prevB.setBorder(BorderFactory.createLineBorder(bColor));
		// nextB.setBorder(BorderFactory.createLineBorder(bColor));

		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));

		statusPanel = new JPanel();
		statusPanel.add(statusLabel);
		statusPanel.setToolTipText("Status");
		statusPanel.setForeground(Color.YELLOW);
		statusPanel.setSize(new Dimension(100, 250));
		statusPanel.setMinimumSize(new Dimension(100, 250));
		// statusPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));

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

	private void addListeners(){
		openFileMenu.addActionListener(new OpenFileListener(this, storage));
		analyseMenu.addActionListener(new AnalyseFileListener(storage));
		simpleAnalyserMenu.addActionListener(new SelectAnalyserListener(storage, new SimpleMetricsAnalyser()));
	}
	
	private void addComponentsToFrame() {
		frame.getContentPane().add(highlightsPanel, BorderLayout.CENTER);
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
		if (statusType == StatusType.ERROR){
			c = Color.RED;
		}else if (statusType == StatusType.NORMAL){
			c = Color.YELLOW;
		}else if (statusType == StatusType.SUCCESS){
			c = Color.GREEN;
		}
		statusLabel.setText(txt);
		statusLabel.setForeground(c);
	}

	@Override
	public Component getFrame() {
		return frame;
	}

	@Override
	public void update(Observable o, Object arg) {
		setStatusText(storage.getStatus(), storage.getStatusType());
	}

}
