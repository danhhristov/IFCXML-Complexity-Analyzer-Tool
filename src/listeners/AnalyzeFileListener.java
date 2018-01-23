package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import core.IAnalyzer;
import core.IStorage;
import util.StatusType;

public class AnalyzeFileListener implements ActionListener {
	private IStorage storage;
	
	public AnalyzeFileListener(IStorage s) {
		storage = s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File subject = storage.getFile();
		IAnalyzer analyzer = storage.getAnalyzer();
		
		if (subject != null && analyzer != null) {
			storage.setStatus("Analyzing...", StatusType.NORMAL);
			analyzer.analyze(subject);
		} else {
			String err = "Select " + ((subject != null) ? "analyzer" : "file");
			storage.setStatus(err, StatusType.ERROR);
		}
	}

}
