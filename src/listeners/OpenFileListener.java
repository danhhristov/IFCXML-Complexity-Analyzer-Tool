package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import core.IStorage;
import gui.IFrame;

public class OpenFileListener implements ActionListener{
	private IFrame frame;
	private IStorage storage;

	public OpenFileListener(IFrame f, IStorage s){
		frame = f;
		storage = s;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame.getFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			storage.setFile(fc.getSelectedFile());
		}
	}

}