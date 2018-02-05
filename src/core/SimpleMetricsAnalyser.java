package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.AnalyserStatus;

public class SimpleMetricsAnalyser extends java.util.Observable implements IAnalyser{
	
	private int status;
	private String fileStats;
	private Map<String, Integer> nodesMap;
	
	public SimpleMetricsAnalyser() {
		status = AnalyserStatus.ERROR;
	}

	@Override
	public void analyse(File subject) {
		nodesMap = new HashMap<String, Integer>();
		setStatus(AnalyserStatus.ANALYSING);
		Document parsedDoc = parseFile(subject);
		Node mainNode = getMainNode(parsedDoc);
		List<Node> children = getChildren(mainNode);
		String nodeName = "";
		Node currNode = null;
		String mostCommon = "";
		int maxCount = 0;
		for(int i = 0; i < children.size(); i ++){
			currNode = children.get(i);
			nodeName = currNode.getNodeName();
			nodeName = nodeName.intern();
			if (nodesMap.containsKey(nodeName)) {
				int newVal = (nodesMap.get(nodeName) + 1);
				nodesMap.replace(nodeName, newVal);
				if(newVal > maxCount){
					maxCount = newVal;
					mostCommon = nodeName;
				}
			} else {
				nodesMap.put(nodeName, 1);
			}
		}
		
		String fileStats = "<html>There are " + children.size() + " nodes in this file."
						+ "<br>Out of those, " + nodesMap.size() + " are unique."
						+ "<br>The most common element in this file is: [" + mostCommon + "]."
						+ "<br>It was seen " + maxCount + " times.</html>";
		setFileStats(fileStats);
		setStatus(AnalyserStatus.COMPLETED);
		update();
	}
	
	private DocumentBuilder createDocumentBuilder(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return builder;
	}
	
	private Document parseFile(File f){
		DocumentBuilder db = createDocumentBuilder();
		if(db == null){
			System.out.println("Error occured while creating the DocumentBuilder");
		}
		Document doc = null;
		try {
			 doc = db.parse(f);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return doc;
	}
	
	private Node getMainNode(Document parsedDoc){
		NodeList children = parsedDoc.getDocumentElement().getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			//Filter out useless comments and other text.
			if (children.item(i).getNodeName().equals("ifc:uos")){
				return children.item(i);
			}
		}
		return null;
	}
	
	private List<Node> getChildren(Node n){
		
		//Consider saving data about node depth and breadth here.
		List<Node> children = new ArrayList<Node>();
		
		if(!n.hasChildNodes()){
			return null;
		}
		
		NodeList currChildren = n.getChildNodes();
		
		for(int i = 0; i < currChildren.getLength(); i++){
			Node node = currChildren.item(i);
			if (node.getNodeName() == "#comment" || node.getNodeName() == "#text") {
				continue;
			}
			children.add(node);
			List<Node> currNodeChildren = getChildren(node);
			if(currNodeChildren != null)
				children.addAll(currNodeChildren);
		}

		return children;
	}
	
	private void update() {
		setChanged();
		notifyObservers();
	}
	
	private void setStatus(int analyserStatus){
		status = analyserStatus;
	}

	@Override
	public int getStatus() {
		return status;
	}
	
	private void setFileStats(String s){
		fileStats = s;
	}

	@Override
	public String getFileStats() {
		return fileStats;
	}

}
