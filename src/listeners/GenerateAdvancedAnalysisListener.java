package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.AdvancedMetricsAnalyser;
import core.IStorage;

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
