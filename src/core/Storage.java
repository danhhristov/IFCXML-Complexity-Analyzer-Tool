package core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;
import javax.swing.table.TableModel;

import util.AnalyserStatus;
import util.CustomTableModel;
import util.StatusType;
import util.TableRow;
import util.UpdateType;

/**
 * A back-end (Model) class that contains and manipulates most of the data
 * and communication with utility classes that perform analysis.
 * 
 * @author Daniel Hristov (2018)
 */
public class Storage extends java.util.Observable implements IStorage, Observer {
	private File subject;
	private String simpleFileStats;
	private int updateType;
	private String status;
	private int statusType;
	private IAnalyser currAnalyser;
	private CustomTableModel tmodel;
	private Timer analyserStarterTimer;

	public Storage() {
		// Default analyser for simple stats
		this.currAnalyser = new SimpleMetricsAnalyser();
		this.currAnalyser.addObserver(this);
		this.tmodel = new CustomTableModel();
	}
	
	/**
	 * notify all observers of a change
	 */
	private void update() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * A customized version of the Observer pattern 'update' method.
	 * This method gets the update status from the observables it observers
	 * and decides how to proceed
	 * @requires The observable this storage is observing to provide a method
	 * which returns an Integer. e.g. AnalyserStatus.Completed
	 */
	@Override
	public void update(Observable o, Object arg) {
		int status = this.currAnalyser.getStatus();
		if (status == AnalyserStatus.COMPLETED) {
			if (this.currAnalyser instanceof SimpleMetricsAnalyser) {
				this.updateType = UpdateType.SIMPLE_STATISTICS;
				setSimpleFileStatistics(currAnalyser.getFileStats());
			} else if (this.currAnalyser instanceof AdvancedMetricsAnalyser) {
				this.updateType = UpdateType.TABLE_STATISTICS;
				resetTable();
				//get all the data from the analyser and add it to the table model
				for(TableRow row: this.currAnalyser.getTableData()){
					addTableRow(row);
				}
				
			}
			setStatus("Analysis Completed", StatusType.SUCCESS);
		} else if (status == AnalyserStatus.ERROR){
			this.updateType = UpdateType.STATUS;
			setStatus("An unexpected error occured. Check  the console for more info.", StatusType.ERROR);
		}
	}
	
	/**
	 * Getters and Setters for the Storage Object's properties
	 */
	
	@Override
	public File getFile() {
		return this.subject;
	}

	@Override
	public void setFile(File f) {
		//Attempt for Garbage collection before setting new file to try and avoid using up all of the allocated memory
		//and/or running out of memory.
		System.gc();
		if (f != null)
			this.subject = f;
		setStatus("File Loaded", StatusType.SUCCESS);
	}

	/**
	 * Sets the status of the app to the given String and updates the status type.
	 * @param msg: String the new app status
	 * @param statusType: Integer a status type e.g. StatusType.SUCCESS
	 */
	@Override
	public void setStatus(String msg, int statusType) {
		this.status = msg;
		this.statusType = statusType;
		update();
	}

	@Override
	public String getStatus() {
		return this.status;
	}

	@Override
	public void setAnalyser(IAnalyser a) {
		this.currAnalyser = a;
		this.currAnalyser.addObserver(this);
		this.updateType = UpdateType.STATUS;
		setStatus("Analyser Set!", StatusType.SUCCESS);
	}

	@Override
	public IAnalyser getAnalyser() {
		return this.currAnalyser;
	}

	@Override
	public int getStatusType() {
		return this.statusType;
	}

	private void setSimpleFileStatistics(String s) {
		this.simpleFileStats = s;
	}

	@Override
	public int getUpdate() {
		return this.updateType;
	}

	@Override
	public String getFileStatistics() {
		return this.simpleFileStats;
	}

	/**
	 * Start analysing the current file with the currently selected Analyser
	 */
	@Override
	public void setStartAnalysis() {
		
		this.updateType = UpdateType.STATUS;
		setStatus("Analysing ...", StatusType.NORMAL);
		analyserStarterTimer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currAnalyser.analyse(getFile());
				analyserStarterTimer.stop();
			}
		});
		analyserStarterTimer.start();

	}

	@Override
	public TableModel getTableModel() {
		return this.tmodel;
	}

	@Override
	public void addTableRow(TableRow row) {
		tmodel.addRow(row);
	}

	@Override
	public TableRow getTableRow(int index) {
		TableRow trow = new TableRow();
		trow.setMetric((String) tmodel.getValueAt(index, 1));
		trow.setValue((String) tmodel.getValueAt(index, 2));
		trow.setMoreInfo((String) tmodel.getValueAt(index, 3));
		System.out.println(trow.getMetric() + ", " + trow.getValue() + ", " + trow.getMoreInfo());
		return trow;
	}
	
	private void resetTable(){
		this.tmodel.clearTable();
	}

}
