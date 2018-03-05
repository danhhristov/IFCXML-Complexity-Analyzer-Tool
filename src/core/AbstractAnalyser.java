package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import util.AnalyserStatus;
import util.SAX_Parser;
import util.XNode;

/**
 * An abstract class that packs all common functionality between analysers.
 * 
 * @author Daniel Hristov (2018)
 */
public abstract class AbstractAnalyser extends Observable implements IAnalyser {
	
	protected int status;
	protected String fileStats;
	
	protected void update(){
		setChanged();
		notifyObservers();
	}
	
	/**
	 * An abstract method to parse a file using the Java SAX parser method.
	 * @param f: File to be parsed.
	 * @param simpleParse: boolean flag to determine whether to perform a simple or a full parse
	 * @return List<XNode> which represents all nodes in this file.
	 */
	protected List<XNode> parseFileSAX(File f, boolean simpleParse) {
		SAX_Parser s = new SAX_Parser(new ArrayList<XNode>());
		try {
			s.parseFile(f, simpleParse);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			//Error occured so update status and notify observers
			setStatus(AnalyserStatus.ERROR);
			update();
		}
		return s.getGeneratedNodes();
	}
	
	
	/**
	 * Getters and Setters...
	 */
	
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
	
	protected int getNumUniqueNodes(List<XNode> nodes){
		int result = 0;
		List<String> visited = new ArrayList<String>();
		for(XNode n : nodes){
			String nodeName = n.getName();
			if(!visited.contains(nodeName)){
				result++;
				visited.add(nodeName);
			}
		}
		return result;
	}
}
