package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Timer;

import core.IAnalyser;
import core.IStorage;
import util.StatusType;

public class AnalyseFileListener implements ActionListener {
	private IStorage storage;
	private Timer t;
	
	public AnalyseFileListener(IStorage s) {
		storage = s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File subject = storage.getFile();
		IAnalyser analyser = storage.getAnalyser();
		
		if (subject != null && analyser != null) {
			storage.setStatus("Analyzing...", StatusType.NORMAL);
			
			t = new Timer(200, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					analyser.analyse(subject);
					t.stop();
				}
			});
			t.start();
			
		} else {
			String err = "Select " + ((subject != null) ? "analyser" : "file");
			storage.setStatus(err, StatusType.ERROR);
		}
	}

}
