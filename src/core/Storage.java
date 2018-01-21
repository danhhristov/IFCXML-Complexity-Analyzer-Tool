package core;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

public class Storage implements IStorage{
	private File subject;
	private List<String> highlights;
	
	public Storage(){
		highlights = new ArrayList<String>();
	}
	
	public File getFile(){
		return subject;
	}
	
	public void setFile(File f){
		if(f != null)
			subject = f;
	}
	
	public void addHighlight(String h){
		highlights.add(h);
	}
	
	public String getHighlight(int index){
		return highlights.get(index);
	}
	
	public List<String> getAllHighlights(){
		return highlights;
	}
}
