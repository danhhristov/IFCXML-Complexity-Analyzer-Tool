package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A custom helper class that is used to parse a file and represent it as different maps and arrays of XNodes.
 * This parser (as the name suggests) uses the Java SAX Parser.
 * 
 * @author Daniel Hristov (2018)
 */
public class SAX_Parser extends DefaultHandler {

	private List<XNode> nodes;
	private Map<String, XNode> referencedNodes;
	private Map<String, XNode> toReference;
	private List<XNode> currNodes;
	private List<XNode> currChildren;
	private boolean isRoot;
	private boolean simpleParse;
	private int id = 0;

	public SAX_Parser(List<XNode> nodes) {
		this.nodes = nodes;
		referencedNodes = new HashMap<String, XNode>();
		toReference = new HashMap<String, XNode>();
		currNodes = new ArrayList<XNode>();
		currChildren = new ArrayList<XNode>();
		isRoot = true;
	}

	/**
	 * 
	 * @param f: File to be parsed
	 * @param simple: boolean to determine whether to perform a "simple" parse or not.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void parseFile(File f, boolean simple) throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		simpleParse = simple;
		parser.parse(f, this);
	}

	public List<XNode> getGeneratedNodes() {
		return nodes;
	}

	@Override
	public void startDocument() throws SAXException {}

	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (simpleParse) {
			//If we are performing a simple parse, then save minimal information and move on, to allow for faster analysis
			XNode node = new XNode(qName);
			assignId(node);
			nodes.add(node);
		} else {
			//If we are performing a full parse, then save all possible data about each node.
			
			if (isRoot) {
				//Filter out initial comments and elements which are not part of the actual project data.
				if(!qName.startsWith("ifc")) return;
				
				//Save this node as the root
				XNode root = new XNode(qName);
				assignId(root);
				//Set the isRoot flag to false so we dont come here again and overwrite anything.
				isRoot = false;
				nodes.add(root);
			} else {
				XNode node = new XNode(qName);
				assignId(node);
				
				//For all children of this node ...
				for (int i = 0; i < attributes.getLength(); i++) {
					
					String attrName = attributes.getQName(i);
					//If the element has an "id" attribute, then other elemets may refer to it
					if (attrName == "id") {
						
						while (toReference.containsKey(attributes.getValue(i))) {
							// This element was referenced before it was initialised, so make sure that all elements that
							// refer to it have the reference associated with it
							toReference.get(attributes.getValue(i)).addReference(node.getId());
							toReference.remove(attributes.getValue(i), toReference.get(attributes.getValue(i)));
						}
						node.setAttrId(attributes.getValue(i));
						//Save this node to the ReferencedNodes map so it can be refered to later.
						referencedNodes.put(attributes.getValue(i), node);
					} else if (attrName == "ref") {
						//This element refers to another element
						
						if (referencedNodes.containsKey(attributes.getValue(i))) {
							//Adding the id (not attribute id) reference to the node so later it can be used for analysis
							node.addReference(referencedNodes.get(attributes.getValue(i)).getId());
						} else {
							// The element has not been seen yet, so save it to a map and reference it later.
							toReference.put(attributes.getValue(i), node);
						}

					}

				}
				
				//Add this node to all nodes;
				nodes.add(node);
				
				// Creating a tree-like structure (adding each element's children) Part(1/2)
				if (!currNodes.isEmpty() && !currChildren.isEmpty()) {
					XNode temp = currNodes.get(currNodes.size() - 1);
					for (int j = 0; j < currChildren.size(); j++) {
						temp.addChidrenId(currChildren.get(j).getId());
					}
					currChildren.clear();
					currNodes.remove(currNodes.size() - 1);
					currNodes.add(temp);

				}
				//Add this node to the list of current Nodes so later when the parent node's end tag is reached, it can be added as a child.
				currNodes.add(node);
			}
		}

	}

	/**
	 * Sets a custom unique ID to the provided Object
	 * 
	 * @param n: XNode to be assigned an ID.
	 */
	private void assignId(XNode n) {
		n.setId("" + id);
		id++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// Creating a tree-like structure (adding each element's children) Part(2/2)
		if (currNodes.isEmpty())
			return;
		XNode temp = currNodes.get(currNodes.size() - 1);
		if (!currChildren.isEmpty()) {
			for (int j = 0; j < currChildren.size(); j++) {
				temp.addChidrenId(currChildren.get(j).getId());
			}
		}
		currChildren.add(temp);
		currNodes.remove(currNodes.size() - 1);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {}

	@Override
	public void endDocument() throws SAXException {
		//Set all objects that are not needed to null to free up some memory.
		referencedNodes = null;
		toReference = null;
		currNodes = null;
		currChildren = null;
		id = 0;
	}
}
