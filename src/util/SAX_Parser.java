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
	public void startDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (simpleParse) {
			XNode node = new XNode(qName);
			assignId(node);
			nodes.add(node);
		} else {
			if (isRoot) {
				if(!qName.startsWith("ifc")) return;
				
				XNode root = new XNode(qName);
				assignId(root);
				isRoot = false;
				nodes.add(root);
			} else {
				XNode node = new XNode(qName);
				assignId(node);
				for (int i = 0; i < attributes.getLength(); i++) {
					String attrName = attributes.getQName(i);
					if (attrName == "id") {
						while (toReference.containsKey(attributes.getValue(i))) {
							// This element was referenced before it was
							// initialised, so make sure that all elements that
							// refer to it have the reference set up
							toReference.get(attributes.getValue(i)).addReference(node.getId());
							toReference.remove(attributes.getValue(i), toReference.get(attributes.getValue(i)));
						}
						node.setAttrId(attributes.getValue(i));
						referencedNodes.put(attributes.getValue(i), node);
					} else if (attrName == "ref") {
						// Adding the id (not attribute id) reference to the
						// node so later it can be used for replacing or
						// something else
						if (referencedNodes.containsKey(attributes.getValue(i))) {
							node.addReference(referencedNodes.get(attributes.getValue(i)).getId());
						} else {
							// The element has not been seen yet, so reference
							// it later.
							toReference.put(attributes.getValue(i), node);
						}

					}

				}
				nodes.add(node);
				// Creating a tree-like structure (adding each element's children)
				if (!currNodes.isEmpty() && !currChildren.isEmpty()) {
					XNode temp = currNodes.get(currNodes.size() - 1);
					for (int j = 0; j < currChildren.size(); j++) {
						temp.addChidrenId(currChildren.get(j).getId());
					}
					currChildren.clear();
					currNodes.remove(currNodes.size() - 1);
					currNodes.add(temp);

				}
				currNodes.add(node);
			}
		}

	}

	private void assignId(XNode n) {
		n.setId("" + id);
		id++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// Creating a tree-like structure (adding each element's children)
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
	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
		referencedNodes = null;
		toReference = null;
		currNodes = null;
		currChildren = null;
		id = 0;
	}
}
