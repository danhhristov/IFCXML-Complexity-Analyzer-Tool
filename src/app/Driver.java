package app;

import core.IStorage;
import core.Storage;
import gui.Frame;
import gui.IFrame;
import util.FrameDetails;

public class Driver {
	public static void main(String[] args) {
		FrameDetails mainWindow = new FrameDetails("IFCXML Complexity Analyser", 800, 700, true);

		IStorage storage = new Storage();
		@SuppressWarnings("unused")
		IFrame frame = new Frame(mainWindow, storage);
	}
}