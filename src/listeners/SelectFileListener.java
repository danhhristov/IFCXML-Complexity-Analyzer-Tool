package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import core.IStorage;
import core.SimpleMetricsAnalyser;
import gui.IFrame;
/**
 * A listener to get the selected file by the user, store it in the Storage object and perform a simple Analysis on it.
 * 
 * @author Daniel Hristov (2018)
 */
public class SelectFileListener implements ActionListener {
	private IFrame frame;
	private IStorage storage;

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
			
			storage.setAnalyser(new SimpleMetricsAnalyser());
			storage.setStartAnalysis();
		}
	}

}
