package app;

import core.IStorage;
import core.Storage;
import gui.Frame;
import gui.IFrame;
import util.FrameDetails;

public class Driver {
	public static void main(String[] args) {
		FrameDetails mainWindow = new FrameDetails("IFCXML Complexity Analyzer", 700, 700, false);

		IStorage storage = new Storage();
		IFrame frame = new Frame(mainWindow, storage);
	}
}