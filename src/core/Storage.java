package core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.StatusType;

public class Storage extends java.util.Observable implements IStorage {
	private File subject;
	private List<String> highlights;
	private String status;
	private int statusType;
	private IAnalyzer currAnalyzer;

	public Storage() {
		highlights = new ArrayList<String>();
	}

	@Override
	public File getFile() {
		return subject;
	}

	@Override
	public void setFile(File f) {
		if (f != null)
			subject = f;
		setStatus("File Loaded", StatusType.SUCCESS);
	}

	@Override
	public void addHighlight(String h) {
		highlights.add(h);
	}

	@Override
	public String getHighlight(int index) {
		return highlights.get(index);
	}

	@Override
	public List<String> getAllHighlights() {
		return highlights;
	}

	@Override
	public void setStatus(String msg, int statusType) {
		status = msg;
		this.statusType = statusType;
		update();
	}

	private void update() {
		setChanged();
		notifyObservers();
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setAnalyzer(IAnalyzer a) {
		currAnalyzer = a;
		setStatus("Analyzer Set!", StatusType.SUCCESS);
	}

	@Override
	public IAnalyzer getAnalyzer() {
		return currAnalyzer;
	}

	@Override
	public int getStatusType() {
		return statusType;
	}

}
