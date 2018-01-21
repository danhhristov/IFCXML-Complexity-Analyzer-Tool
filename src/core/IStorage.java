package core;

import java.io.File;
import java.util.List;

public interface IStorage {
	
	public File getFile();
	public void setFile(File f);
	
	public void addHighlight(String highlight);
	public String getHighlight(int index);
	public List<String> getAllHighlights();
}
