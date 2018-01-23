package testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Driver {
	private static JFrame frame;
	private static JMenuBar menubar;
	private static JMenu fileMenu;
	private static JMenuItem openFileMenu;
	private static JPanel highlightsPanel;
	private static JPanel navigationPanel;
	private static JButton prevB;
	private static JButton nextB;
	private static JPanel statusPanel;
	private static File subject = null;
	private static Map<String, Integer> nodesMap = new HashMap<String, Integer>();

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		frame = new JFrame("Simple Stats");
		frame.setSize(new Dimension(700, 700));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		openFileMenu = new JMenuItem("Open...");

		highlightsPanel = new JPanel();
		highlightsPanel.setSize(new Dimension(500, 650));
		highlightsPanel.setMinimumSize(new Dimension(500, 650));
		highlightsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		JLabel tempInfo = new JLabel("Highlights Panel");
		tempInfo.setFont(new Font(Font.SANS_SERIF, 1, 70));
		highlightsPanel.add(tempInfo, BorderLayout.CENTER);

		navigationPanel = new JPanel();
		navigationPanel.setSize(new Dimension(200, 650));
		navigationPanel.setMinimumSize(new Dimension(200, 650));
		navigationPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

		Dimension buttonDimensions = new Dimension(150, 300);
		prevB = new JButton("Previous");
		nextB = new JButton("Next");
		prevB.setSize(buttonDimensions);
		nextB.setSize(buttonDimensions);
		prevB.setFont(new Font(Font.SANS_SERIF, 1, 32));
		nextB.setFont(new Font(Font.SANS_SERIF, 1, 32));
		prevB.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		nextB.setBorder(BorderFactory.createLineBorder(Color.GREEN));

		JLabel statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font(Font.SANS_SERIF, 1, 24));

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
		openFileMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					subject = fc.getSelectedFile();
					// This is where a real application would open the file.
					System.out.println("Opening: " + subject.getName() + ".");

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

					DocumentBuilder builder = null;
					try {
						builder = factory.newDocumentBuilder();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					}
					Document document = null;
					try {
						document = builder.parse(subject);
					} catch (SAXException | IOException e1) {
						e1.printStackTrace();
					}

					NodeList nodeList = document.getDocumentElement().getChildNodes();
					int counter = 0;
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node curr = nodeList.item(i);
						String name = curr.getNodeName();
						name = name.intern();
						// name.intern() - map strings to save space - hold
						// refferences instead of values.

						/*
						 * Custom Class Node structure: array of "int" for
						 * metric values like depth width and so on bit array of
						 * attributes - 10001100 - to say that it has those
						 * attributes in the "1" possitions e.g - it has
						 * attribute 1, attribute 4 and 5 and no others then
						 * have a look up table to say what each attribute is
						 * e.g. Attribute 1 is ID, Attribute 4 is colour , etc.
						 */

						if (curr.getNodeName() == "#comment" || curr.getNodeName() == "#text") {
							continue;
						}

						// System.out.println(curr.getNodeName());

						if (nodesMap.containsKey(name)) {
							nodesMap.replace(name, (nodesMap.get(name) + 1));
						} else {
							nodesMap.put(name, 1);
						}

						counter++;
						for (Node n : getAllNodes(curr)) {
							String nodeName = n.getNodeName();
							nodeName = nodeName.intern();

							if (nodesMap.containsKey(nodeName)) {
								nodesMap.replace(nodeName, (nodesMap.get(nodeName) + 1));
							} else {
								nodesMap.put(nodeName, 1);
							}

							// System.out.println(n.getNodeName());
							counter++;
						}

					}

					System.out.println("The file contains " + counter + " nodes.");
					nodesMap = MapUtil.sortByValue(nodesMap);
					for (String key : nodesMap.keySet()) {
						System.out.println("Node " + key + " has " + nodesMap.get(key) + " instances");
					}

				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});

		frame.getContentPane().add(highlightsPanel, BorderLayout.CENTER);
		frame.getContentPane().add(navigationPanel, BorderLayout.PAGE_END);
		frame.setJMenuBar(menubar);
		frame.setVisible(true);

	}

	private static List<Node> getAllNodes(Node node) {

		// save data about depth and bredth of the node
		if (!node.hasChildNodes()) {
			return new ArrayList<Node>();
		}
		List<Node> allChildren = new ArrayList<Node>();

		NodeList children = node.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node currChild = children.item(i);
			if (currChild.getNodeName() == "#comment" || currChild.getNodeName() == "#text") {
				continue;
			}
			allChildren.add(currChild);

			allChildren.addAll(getAllNodes(currChild));

		}

		return allChildren;
	}
}
