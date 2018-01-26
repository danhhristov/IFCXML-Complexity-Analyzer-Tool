package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.IAnalyser;
import core.IStorage;

public class SelectAnalyserListener implements ActionListener {
	
	private IStorage storage;
	private IAnalyser analyser;
	
	public SelectAnalyserListener(IStorage s, IAnalyser a){
		storage = s;
		analyser = a;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		storage.setAnalyser(analyser);
	}

}
