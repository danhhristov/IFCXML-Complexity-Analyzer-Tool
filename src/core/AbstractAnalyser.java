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
}
