package core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import util.AnalyserStatus;

public class SimpleMetricsAnalyser extends AbstractAnalyser implements IAnalyser{
	
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
		
		List<Node> children = getNodes(subject);
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
						+ "<br>Most Common: " + mostCommon + ", seen " +  maxCount + " times."
						+ "<br>File Size " + this.fileSize + this.fileSizeUnit
						+ "<br>Breadth " + this.breadth
						+ "<br>Depth " + this.depth + "</html>";
		setFileStats(fileStats);
		setStatus(AnalyserStatus.COMPLETED);
		update();
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
