package core;

import java.io.File;
import java.util.List;

import util.AnalyserStatus;
import util.XNode;

public class AdvancedMetricsAnalyser extends AbstractAnalyser {

	@Override
	public void analyse(File f) {
		setStatus(AnalyserStatus.ANALYSING);

		boolean simpleParse = false;
		List<XNode> nodes = parseFileSAX(f, simpleParse);


		setFileStats("");

		setStatus(AnalyserStatus.COMPLETED);
		update();
		/*
		 * 
		 * 
		 * nodesMap = new HashMap<String, Integer>();
		 * 
		 * 
		 * List<Node> children = getNodes(subject); String nodeName = ""; Node
		 * currNode = null;
		 * 
		 * int maxCount = 0; for (int i = 0; i < children.size(); i++) {
		 * currNode = children.get(i); nodeName = currNode.getNodeName();
		 * nodeName = nodeName.intern(); if (nodesMap.containsKey(nodeName)) {
		 * int newVal = (nodesMap.get(nodeName) + 1); nodesMap.replace(nodeName,
		 * newVal); if (newVal > maxCount) { maxCount = newVal; mostCommon =
		 * nodeName; } } else { nodesMap.put(nodeName, 1); } }
		 * 
		 * String deepestNodePath = ""; String tabs = "&emsp;"; Node root =
		 * this.deepestNode; int safeCounter = 0; while (root != this.root &&
		 * safeCounter < 50) { deepestNodePath = root.getNodeName() + "<br>" +
		 * tabs + deepestNodePath; root = root.getParentNode(); safeCounter++; }
		 * 
		 * deepestNodePath = root.getNodeName() + "<br>" + tabs +
		 * deepestNodePath;
		 * 
		 * String fileStats = "<html>There are " + children.size() +
		 * " nodes in this file." + "<br>Out of those, " + nodesMap.size() +
		 * " are unique." + "<br>Most Common: " + mostCommon + ", seen " +
		 * maxCount + " times." + "<br>File Size " + this.fileSize +
		 * this.fileSizeUnit + "<br>Breadth " + this.breadth +
		 * "<br>Path to deepest node:<br>" + deepestNodePath + "<br>Depth " +
		 * this.depth + "</html>"; setFileStats(fileStats);
		 */

	}


}
