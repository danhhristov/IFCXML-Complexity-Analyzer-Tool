package core;

import java.io.File;
import java.util.List;
import java.util.Observer;

import util.TableRow;

public interface IAnalyser {
	public void analyse(File f);
	public void addObserver(Observer o);
	public List<TableRow> getTableData();
	public String getFileStats();
	public int getStatus();
}
