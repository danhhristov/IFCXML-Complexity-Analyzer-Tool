package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractAnalyser extends Observable implements IAnalyser{
	
	public int depth = 1;
	public int breadth = 1;
	public long fileSize = 0;
	public String fileSizeUnit = "";
	private int fileSizeUnitsIndex = 0;
	private String[] fileSizeUnits = {"B" , "KB", "MB", "GB", "TB"};

	public List<Node> getNodes(File f){
		resetStats();
		calcFileSize(f);
		Document parsedDoc = parseFile(f);
		Node mainNode = getMainNode(parsedDoc);
		List<Node> elements = getChildren(mainNode, 1);
		return elements;
	}
	
	private void resetStats(){
		this.depth = 1;
		this.breadth = 1;
		this.fileSize = 0;
		this.fileSizeUnitsIndex = 0;
	}
	
	private void calcFileSize(File f){
		fileSize = f.length();
		while(fileSize > 1024){
			fileSize = fileSize / 1024;
			fileSizeUnitsIndex ++;
		}
		fileSizeUnit = fileSizeUnits[fileSizeUnitsIndex];
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
	
	private List<Node> getChildren(Node n, int depth){
		
		//Consider saving data about node depth and breadth here.
		List<Node> children = new ArrayList<Node>();
		
		if(!n.hasChildNodes()){
			if(this.depth < depth){
				this.depth = depth;
			}
			return null;
		}
		
		NodeList currChildren = n.getChildNodes();
		
		for(int i = 0; i < currChildren.getLength(); i++){
			Node node = currChildren.item(i);
			if (node.getNodeName() == "#comment" || node.getNodeName() == "#text") {
				continue;
			}
			if(depth == 1){
				this.breadth ++;
			}
			children.add(node);
			List<Node> currNodeChildren = getChildren(node, depth + 1);
			if(currNodeChildren != null)
				children.addAll(currNodeChildren);
		}

		return children;
	}
}
