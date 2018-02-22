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

	@Override
	public File getFile() {
		return this.subject;
	}

	@Override
	public void setFile(File f) {
		//Garbage collection before setting new file.
		System.gc();
		if (f != null)
			this.subject = f;
		setStatus("File Loaded", StatusType.SUCCESS);
	}

	@Override
	public void setStatus(String msg, int statusType) {
		this.status = msg;
		this.statusType = statusType;
		update();
	}

	private void update() {
		setChanged();
		notifyObservers();
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
		}
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
