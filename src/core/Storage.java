package core;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import util.AnalyserStatus;
import util.StatusType;
import util.UpdateType;

public class Storage extends java.util.Observable implements IStorage, Observer {
	private File subject;
	private String fileStats;
	private int updateType;
	private String status;
	private int statusType;
	private IAnalyser currAnalyser;

	public Storage() {
		currAnalyser = new SimpleMetricsAnalyser();
		currAnalyser.addObserver(this);
		//Default analyser
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
	public void setAnalyser(IAnalyser a) {
		currAnalyser = a;
		currAnalyser.addObserver(this);
		updateType = UpdateType.STATUS;
		setStatus("Analyser Set!", StatusType.SUCCESS);
	}

	@Override
	public IAnalyser getAnalyser() {
		return currAnalyser;
	}

	@Override
	public int getStatusType() {
		return statusType;
	}

	@Override
	public void update(Observable o, Object arg) {
		int status = currAnalyser.getStatus();
		if (status == AnalyserStatus.COMPLETED){
			updateType = UpdateType.STATISTICS;
			setFileStatistics(currAnalyser.getFileStats());
			setStatus("Analysis Completed", StatusType.SUCCESS);
		}
	}
	
	private void setFileStatistics(String s){
		fileStats = s;
	}
	
	@Override
	public int getUpdate(){
		return updateType;
	}

	@Override
	public String getFileStatistics() {
		return fileStats;
	}

	@Override
	public void setStartAnalysis() {
		updateType = UpdateType.STATUS;
		setStatus("Analysing ...", StatusType.NORMAL);
	}

}
