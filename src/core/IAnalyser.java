package core;

import java.io.File;
import java.util.List;
import java.util.Observer;

import util.TableRow;

/**
 * An Interface for all analysers.
 * 
 * @author Daniel Hristov (2018)
 */

public interface IAnalyser {
	public void analyse(File f);
	public void addObserver(Observer o);
	public List<TableRow> getTableData();
	public String getFileStats();
	public int getStatus();
}
