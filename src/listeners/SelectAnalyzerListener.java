package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.IAnalyzer;
import core.IStorage;

public class SelectAnalyzerListener implements ActionListener {
	
	private IStorage storage;
	private IAnalyzer analyzer;
	
	public SelectAnalyzerListener(IStorage s, IAnalyzer a){
		storage = s;
		analyzer = a;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		storage.setAnalyzer(analyzer);
	}

}
