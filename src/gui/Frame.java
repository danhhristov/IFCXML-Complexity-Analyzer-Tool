package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import core.IStorage;
import util.FrameDetails;

public class Frame implements IFrame{
	private JFrame frame;
	private JMenuBar menubar;
	private JMenu fileMenu;
	private JMenuItem openFileMenu;
	private JPanel highlightsPanel;
	private JLabel highlightsLabel;
	private JPanel navigationPanel;
	private JButton prevB;
	private JButton nextB;
	private JPanel statusPanel;
	private JLabel statusLabel;
	
	public Frame(FrameDetails fd, IStorage s){
		initFrame(fd);
		initComponents();
		addComponentsToFrame();
		frame.setVisible(true);
	}
	
	private void initFrame(FrameDetails fd){
		frame = new JFrame(fd.title);
		frame.setSize(new Dimension(fd.width, fd.height));
		frame.setResizable(fd.resizable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initComponents(){
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		openFileMenu = new JMenuItem("Open...");
		
		highlightsPanel = new JPanel();
		highlightsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		highlightsLabel = new JLabel("Highlights Panel");
		highlightsLabel.setFont(new Font(Font.SANS_SERIF,1,70));
		highlightsPanel.add(highlightsLabel, BorderLayout.CENTER);
		
		navigationPanel = new JPanel();
		navigationPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		Dimension bDimensions = new Dimension(150, 300);
		Font bFont = new Font(Font.SANS_SERIF,1,32);
		Color bColor = Color.GREEN;
		prevB = new JButton("Previous");
		nextB = new JButton("Next");
		prevB.setSize(bDimensions);
		nextB.setSize(bDimensions);
		prevB.setFont(bFont);
		nextB.setFont(bFont);
		prevB.setBorder(BorderFactory.createLineBorder(bColor));
		nextB.setBorder(BorderFactory.createLineBorder(bColor));
		
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF,1,24));
		
		statusPanel = new JPanel();
		statusPanel.add(statusLabel);
		statusPanel.setToolTipText("Status");
		statusPanel.setForeground(Color.YELLOW);
		statusPanel.setSize(new Dimension(100, 250));
		statusPanel.setMinimumSize(new Dimension(100, 250));
		statusPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		
		navigationPanel.add(prevB, BorderLayout.EAST);
		navigationPanel.add(statusPanel, BorderLayout.CENTER);
		navigationPanel.add(nextB, BorderLayout.WEST);		
		
		fileMenu.add(openFileMenu);
		menubar.add(fileMenu);
	}
	
	private void addComponentsToFrame(){
		frame.getContentPane().add(highlightsPanel, BorderLayout.CENTER);
		frame.getContentPane().add(navigationPanel, BorderLayout.PAGE_END);
		frame.setJMenuBar(menubar);
	}
	
	public void setVisible(boolean b){
		frame.setVisible(b);
	}
	
	public Component[] getComponents(){
		return frame.getContentPane().getComponents();
	}
	
	public void setStatusText(String txt){
		statusLabel.setText(txt);
	}
	
	public Component getFrame(){
		return frame;
	}

}
