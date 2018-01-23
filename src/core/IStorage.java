package core;

import java.io.File;
import java.util.List;

public interface IStorage {

	public File getFile();

	public void setFile(File f);

	public void addHighlight(String highlight);

	public String getHighlight(int index);

	public List<String> getAllHighlights();

	public String getStatus();

	public void addObserver(java.util.Observer o);
	
	public void setAnalyzer(IAnalyzer a);
	
	public IAnalyzer getAnalyzer();
	
	public void setStatus(String msg, int statusType);
	
	public int getStatusType();
}
