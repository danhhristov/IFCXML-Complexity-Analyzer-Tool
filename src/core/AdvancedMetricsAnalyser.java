package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.AnalyserStatus;
import util.TableRow;
import util.XNode;

/**
 * An analyser which performs detailed analysis over a given file.
 * The produced statistics are saved locally and can be later on accessed via the getTableData() method
 * 
 * @author Daniel Hristov (2018)
 */
public class AdvancedMetricsAnalyser extends AbstractAnalyser {
	private Map<String, XNode> nodes;
	private Map<String, Integer> referals;
	private List<TableRow> metricsInfo;
	
	public AdvancedMetricsAnalyser() {
		nodes = new HashMap<String, XNode>();
		referals = new HashMap<String, Integer>();
		metricsInfo = new ArrayList<TableRow>();
	}

	/**
	 * Analyse a file and produce detailed statistics (metrics) about it.
	 * @param f: File to be analysed
	 */
	@Override
	public void analyse(File f) {
		
		setStatus(AnalyserStatus.ANALYSING);
		
		clearPreviousResults();
		
		boolean simpleParse = false;
		List<XNode> nodesList = parseFileSAX(f, simpleParse);
		
		//set up all needed variables for the following calculations
		int totalChildren = 0;
		int maxChildren = 0;
		int maxReferalsNum = 0;
		int totalReferals = 0;
		float avgReferals = 0 ;
		
		XNode biggestParent = null;
		String mostRefered = "";
		
		//Finding node with most children, summing up referals, finding most refered element and adding all children up
		//so later it can be used to produce other statistics
		for(XNode n: nodesList){
			nodes.put(n.getId(), n);
			
			//Referals calculations
			if(n.getReferences().size() > 0){
				if(referals.containsKey(n.getReferences().get(0))){
					int newReferalAmount = referals.get(n.getReferences().get(0)) + 1;
					referals.replace(n.getReferences().get(0), newReferalAmount);
					if(newReferalAmount > maxReferalsNum){
						maxReferalsNum = newReferalAmount;
						mostRefered = n.getReferences().get(0);
					}
				}else{
					referals.put(n.getReferences().get(0), 1);
				}
			}
			
			//Summing all children up and finding the node with most children
			int currChildren = n.getChildrenIDs().size();
			totalChildren += currChildren;
			if(currChildren > maxChildren){
				maxChildren = currChildren;
				biggestParent = n;
			}
		}
		
		for(Integer x: referals.values()){
			totalReferals += x;
		}
		
		//Double digit rounding function looked up from: 
		//https://www.mkyong.com/java/how-to-round-double-float-value-to-2-decimal-points-in-java/
		//Divide only by the number of refered elements to get an accurate count.
		avgReferals = (float) (Math.round((float) totalReferals / referals.size() *100.0)/100.0);
		mostRefered = nodes.get(mostRefered).getName();
		
		//File size, unique elements and total elements calculations
		float fileSizeL = f.length();
		float uniqueElements = getNumUniqueNodes(nodesList);
		float totalElements = nodes.size();
		//Calculating relative Complexity and Complexity Score
		
		float relComplexity = (float) Math.abs(Math.pow(Math.abs(10 - Math.abs((Math.abs(fileSizeL / totalElements) * 20) / uniqueElements )), 2) / avgReferals) * 2;
		float complexityScore = (float) Math.abs((Math.abs((fileSizeL / (8*1024)) / uniqueElements) * relComplexity) / (fileSizeL / totalElements)) * 10;
		
		//Assigning the correct "development stage" to the file
		String complexityScoreStage = "";
		if(complexityScore > 400)
			complexityScoreStage = "Decorative";
		if(complexityScore <= 400)
			complexityScoreStage = "Development";
		if(complexityScore <= 50)
			complexityScoreStage = "Design";
		
		
		//Calculating file size (easy to read version)
		String[] fileSizeUnits = {"B", "KB", "MB", "GB", "TB"};
		int unitsIndex = 0;
		while(fileSizeL > 1024){
			fileSizeL = fileSizeL / 1024;
			unitsIndex++;
		}
		String fileSize = Math.round(fileSizeL) + fileSizeUnits[unitsIndex];
		

		//Adding all produced metrics to the List of metrics.
		addMetric("File Name", f.getName(), fileSize);
		addMetric("Average number of children" , "" + Math.round((totalChildren / totalElements)*100.0)/100.0, "");
		addMetric("Element with most children" , biggestParent.getName(), maxChildren + " child elements");
		addMetric("Most refered Element", mostRefered, maxReferalsNum + " number of referals");
		addMetric("Total references", ""+totalReferals, "for " + referals.size() + " refered elements");
		addMetric("Average references", ""+avgReferals, "");
		addMetric("Complexity Score", Math.round(complexityScore) + "", complexityScoreStage + " stage");

		
		//Analysis completed, so update the status and notify observers.
		setStatus(AnalyserStatus.COMPLETED);
		update();

	}

	/**
	 * Reset the objects so they can be used again.
	 */
	private void clearPreviousResults(){
		nodes = null;
		metricsInfo = null;
		referals = null;
		nodes = new HashMap<String, XNode>();
		referals = new HashMap<String, Integer>();
		metricsInfo = new ArrayList<TableRow>();
	}
	
	private void addMetric(String metric, String val, String optional){
		TableRow row = new TableRow();
		row.setMetric(metric);
		row.setValue(val);
		row.setMoreInfo(optional);
		metricsInfo.add(row);
	}

	@Override
	public List<TableRow> getTableData() {
		return metricsInfo;
	}

}
