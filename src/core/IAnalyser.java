package core;

import java.io.File;
import java.util.Observer;

public interface IAnalyser {
	public void analyse(File f);
	public void addObserver(Observer o);
}
