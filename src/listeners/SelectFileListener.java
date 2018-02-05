package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.Timer;

import core.IAnalyser;
import core.IStorage;
import gui.IFrame;

public class SelectFileListener implements ActionListener {
	private IFrame frame;
	private IStorage storage;
	private Timer t;

	public SelectFileListener(IFrame f, IStorage s) {
		frame = f;
		storage = s;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame.getFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			storage.setFile(fc.getSelectedFile());
			
			IAnalyser analyser = storage.getAnalyser();
			
			storage.setStartAnalysis();
			t = new Timer(200, new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					analyser.analyse(storage.getFile());
					t.stop();
				}
			});
			t.start();
		}
	}

}
