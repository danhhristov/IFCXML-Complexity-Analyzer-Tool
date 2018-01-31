package core;

import java.io.File;
import java.util.Observer;

public interface IStorage {

	public File getFile();

	public void setFile(File f);

	public String getFileStatistics();

	public String getStatus();

	public void addObserver(Observer o);
	
	public void setAnalyser(IAnalyser a);
	
	public IAnalyser getAnalyser();
	
	public void setStatus(String msg, int statusType);
	
	public int getStatusType();
	
	public int getUpdate();
}
