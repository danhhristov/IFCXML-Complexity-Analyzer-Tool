package core;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

public class Storage extends java.util.Observable implements IStorage{
	private File subject;
	private List<String> highlights;
	private String status;
	
	public Storage(){
		highlights = new ArrayList<String>();
	}
	
	public File getFile(){
		return subject;
	}
	
	public void setFile(File f){
		if(f != null)
			subject = f;
		updateStatus("File Loaded");
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
	
	private void updateStatus(String msg){
		status = msg;
		update();
	}
	
	private void update() {
		setChanged();
		notifyObservers();
	}
	
	public String getStatus(){
		return status;
	}
}
