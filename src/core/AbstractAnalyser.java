package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import util.SAX_Parser;
import util.XNode;

public abstract class AbstractAnalyser extends Observable implements IAnalyser {
	
	protected int depth = 1;
	protected int breadth = 1;
	protected Node deepestNode;
	protected Node root;
	
	protected int status;
	protected String fileStats;

//	protected List<Node> getNodes(File f) {
//		resetStats();
//		Document parsedDoc = parseFileDOM(f);
//		root = getMainNode(parsedDoc);
//		List<Node> elements = getChildren(root, 1);
//		return elements;
//	}
	
	protected void update(){
		setChanged();
		notifyObservers();
	}
	
	public int getStatus(){
		return this.status;
	}
	
	protected void setStatus(int analyserStatus) {
		this.status = analyserStatus;
	}
	
	protected void setFileStats(String s) {
		fileStats = s;
	}
	
	public String getFileStats() {
		return fileStats;
	}

	private void resetStats() {
		this.depth = 1;
		this.breadth = 1;
	}


//	private Document parseFileDOM(File f) {
//		DocumentBuilder db = createDocumentBuilder();
//		if (db == null) {
//			System.out.println("Error occured while creating the DocumentBuilder");
//		}
//		Document doc = null;
//		try {
//			doc = db.parse(f);
//		} catch (SAXException | IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//
//		return doc;
//	}

	protected List<XNode> parseFileSAX(File f, boolean simpleParse) {
		resetStats();
		SAX_Parser s = new SAX_Parser(new ArrayList<XNode>());
		try {
			s.parseFile(f, simpleParse);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return s.getGeneratedNodes();
		
	}

//	private DocumentBuilder createDocumentBuilder() {
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//		DocumentBuilder builder = null;
//		try {
//			builder = factory.newDocumentBuilder();
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//		return builder;
//	}
//
//	private Node getMainNode(Document parsedDoc) {
//		NodeList children = parsedDoc.getDocumentElement().getChildNodes();
//		for (int i = 0; i < children.getLength(); i++) {
//			// Filter out useless comments and other text.
//			if (children.item(i).getNodeName().equals("ifc:uos")) {
//				return children.item(i);
//			}
//		}
//		return null;
//	}

//	private List<Node> getChildren(Node n, int depth) {
//
//		// Consider saving data about node depth and breadth here.
//		List<Node> children = new ArrayList<Node>();
//
//		if (!n.hasChildNodes()) {
//			if (this.depth < depth) {
//				this.depth = depth;
//				deepestNode = n;
//			}
//			return null;
//		}
//
//		NodeList currChildren = n.getChildNodes();
//
//		for (int i = 0; i < currChildren.getLength(); i++) {
//			Node node = currChildren.item(i);
//			if (node.getNodeName() == "#comment" || node.getNodeName() == "#text") {
//				continue;
//			}
//			if (depth == 1) {
//				this.breadth++;
//			}
//			children.add(node);
//			List<Node> currNodeChildren = getChildren(node, depth + 1);
//			if (currNodeChildren != null)
//				children.addAll(currNodeChildren);
//		}
//
//		return children;
//	}
}
