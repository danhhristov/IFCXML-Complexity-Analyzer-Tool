package core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.AnalyserStatus;
import util.XNode;

public class SimpleMetricsAnalyser extends AbstractAnalyser implements IAnalyser {

	public SimpleMetricsAnalyser() {}

	@Override
	public void analyse(File f) {
		setStatus(AnalyserStatus.ANALYSING);
		
		String mostCommonName = "";
		int maxCount = 0;
		
		Map<String, Integer> nodesMap = new HashMap<String, Integer>();
		boolean simpleParse = true;
		List<XNode> nodes = parseFileSAX(f, simpleParse);
		for(XNode n : nodes){
			String nodeName = n.getName();
			if(nodesMap.containsKey(nodeName)){
				int newCount = nodesMap.get(nodeName) + 1;
				nodesMap.replace(nodeName, newCount);
				if(newCount > maxCount){
					maxCount = newCount;
					mostCommonName = nodeName;
				}
			}else{
				nodesMap.put(n.getName(), 1);
			}
		}
		
		String fileStats = "<html>There are " + nodes.size() + " nodes in this file." + "<br>Out of those, "
				+ nodesMap.size() + " are unique." + "<br>Most Common: " + mostCommonName + ", seen " + maxCount + " times.</html>";
		setFileStats(fileStats);
		
		setStatus(AnalyserStatus.COMPLETED);
		update();
	}

}
