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
	private Map<String, Integer> referrals;
	private List<TableRow> metricsInfo;
	
	public AdvancedMetricsAnalyser() {
		nodes = new HashMap<String, XNode>();
		referrals = new HashMap<String, Integer>();
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
		int maxReferralsNum = 0;
		int totalReferrals = 0;
		float avgReferrals = 0 ;
		
		XNode biggestParent = null;
		String mostReferred = "";
		
		//Finding node with most children, summing up referrals, finding most referred node and adding all children up
		//so later it can be used to produce other statistics
		for(XNode n: nodesList){
			nodes.put(n.getId(), n);
			
			//Referrals calculations
			if(n.getReferences().size() > 0){
				if(referrals.containsKey(n.getReferences().get(0))){
					int newReferralAmount = referrals.get(n.getReferences().get(0)) + 1;
					referrals.replace(n.getReferences().get(0), newReferralAmount);
					if(newReferralAmount > maxReferralsNum){
						maxReferralsNum = newReferralAmount;
						mostReferred = n.getReferences().get(0);
					}
				}else{
					referrals.put(n.getReferences().get(0), 1);
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
		
		for(Integer x: referrals.values()){
			totalReferrals += x;
		}
		
		//Double digit rounding function looked up from: 
		//https://www.mkyong.com/java/how-to-round-double-float-value-to-2-decimal-points-in-java/
		//Divide only by the number of referred nodes to get an accurate count.
		avgReferrals = (float) (Math.round((float) totalReferrals / referrals.size() *100.0)/100.0);
		mostReferred = nodes.get(mostReferred).getName();
		
		//File size, unique nodes and total nodes calculations
		float fileSizeL = f.length();
		float uniqueNodes = getNumUniqueNodes(nodesList);
		float totalNodes = nodes.size();
		//Calculating relative Complexity and Complexity Score
		
		float relComplexity = (float) Math.abs(Math.pow(Math.abs(10 - Math.abs((Math.abs(fileSizeL / totalNodes) * 20) / uniqueNodes )), 2) / avgReferrals) * 2;
		float complexityScore = (float) Math.abs((Math.abs((fileSizeL / (8*1024)) / uniqueNodes) * relComplexity) / (fileSizeL / totalNodes)) * 10;
		
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
		addMetric("Average number of children" , "" + Math.round((totalChildren / totalNodes)*100.0)/100.0, "");
		addMetric("Node with most children" , biggestParent.getName(), maxChildren + " child nodes");
		addMetric("Most referred Node", mostReferred, maxReferralsNum + " number of referrals");
		addMetric("Total references", ""+totalReferrals, "for " + referrals.size() + " referred nodes");
		addMetric("Average references", ""+avgReferrals, "");
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
		referrals = null;
		nodes = new HashMap<String, XNode>();
		referrals = new HashMap<String, Integer>();
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
