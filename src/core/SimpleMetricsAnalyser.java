package core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.AnalyserStatus;
import util.TableRow;
import util.XNode;
/**
 * An analyser which performs simple analysis on a given file.
 * The produced statistics are saved locally and can be later on accessed via the abstract method getFileStats()
 * 
 * @author Daniel Hristov (2018)
 */
public class SimpleMetricsAnalyser extends AbstractAnalyser{

	/**
	 * Analyse a file and produce some simple statistics about it.
	 * @param f: File to be analysed
	 */
	@Override
	public void analyse(File f) {
		setStatus(AnalyserStatus.ANALYSING);
		
		String mostCommonName = "";
		int maxCount = 0;
		
		Map<String, Integer> nodesMap = new HashMap<String, Integer>();
		
		boolean simpleParse = true;
		List<XNode> nodes = parseFileSAX(f, simpleParse);
		
		//Find most common node
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
		
		//Save the produced statistics
		String fileStats = "<html>There are " + nodes.size() + " nodes in this file." + "<br>Out of those, "
				+ nodesMap.size() + " are unique." + "<br>Most Common: " + mostCommonName + ", seen " + maxCount + " times.</html>";
		setFileStats(fileStats);
		
		//Update the analyser status and notify all observers
		setStatus(AnalyserStatus.COMPLETED);
		update();
	}

	/**
	 * Method not used by this Class but needed to avoid Casting in other parts of the program
	 */
	@Override
	public List<TableRow> getTableData() { return null;}

}
