package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.AdvancedMetricsAnalyser;
import core.IStorage;

/**
 * A listener for setting the storage's analyser to AdvancedMetricsAnalyser and performing analysis
 * 
 * @author Daniel Hristov (2018)
 */
public class GenerateAdvancedAnalysisListener implements ActionListener{
	
	private IStorage storage;
	
	public GenerateAdvancedAnalysisListener(IStorage s){
		storage = s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		storage.setAnalyser(new AdvancedMetricsAnalyser());
		storage.setStartAnalysis();
	}

}
