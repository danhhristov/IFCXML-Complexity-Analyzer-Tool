package util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAX_Parser extends DefaultHandler {

	private List<XNode> nodes;
	private boolean isRoot;
	private boolean simpleParse;
	private int id = 0;

	public SAX_Parser(List<XNode> nodes) {
		this.nodes = nodes;
		isRoot = true;
	}

	public void parseFile(File f, boolean simple) throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		simpleParse = simple;
		parser.parse(f, this);
	}
	
	public List<XNode> getGeneratedNodes(){
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
				XNode root = new XNode(localName);
				assignId(root);
				isRoot = false;
				nodes.add(root);
			} else {
				XNode node = new XNode(localName);
				assignId(node);
				nodes.add(node);
			}
		}
		
	}
	
	private void assignId(XNode n){
		n.setId(id);
		id++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (simpleParse) {

		} else {

		}
	}

	@Override
	public void endDocument() throws SAXException {
	}

}
