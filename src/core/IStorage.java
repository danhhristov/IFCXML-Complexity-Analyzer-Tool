package core;

import java.io.File;
import java.util.Observer;

import javax.swing.table.TableModel;

import util.TableRow;

/**
 * An Interface for the Storage (Model) class.
 * 
 * @author Daniel Hristov (2018)
 */
public interface IStorage {

	public File getFile();

	public void setFile(File f);

	public String getFileStatistics();
	
	public TableModel getTableModel();
	
	public void addTableRow(TableRow row);
	
	public TableRow getTableRow(int index);

	public String getStatus();

	public void addObserver(Observer o);
	
	public void setAnalyser(IAnalyser a);
	
	public void setStartAnalysis();
	
	public IAnalyser getAnalyser();
	
	public void setStatus(String msg, int statusType);
	
	public int getStatusType();
	
	public int getUpdate();
}
