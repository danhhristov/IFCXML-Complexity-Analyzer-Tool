package util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columns = { "Metric Description", "Value", "More info" };
	private List<String> metrics;
	private List<String> values;
	private List<String> moreInfo;

	public CustomTableModel() {
		metrics = new ArrayList<String>();
		values = new ArrayList<String>();
		moreInfo = new ArrayList<String>();
	}

	@Override
	public int getRowCount() {
		return metrics.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columns[col];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return metrics.get(rowIndex);
		} else if (columnIndex == 1) {
			return values.get(rowIndex);
		} else if (columnIndex == 2) {
			return moreInfo.get(rowIndex);
		}
		return null;
	}
	
	public void setValueAt(String value, int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			metrics.remove(rowIndex);
			metrics.add(rowIndex, value);
		} else if (columnIndex == 1) {
			values.remove(rowIndex);
			values.add(rowIndex, value);
		} else if (columnIndex == 2) {
			moreInfo.remove(rowIndex);
			moreInfo.add(rowIndex, value);
		}
	}
	
	public void addRow(TableRow row){
		if (row.getMetric() != null && row.getValue() != null) {
			metrics.add(row.getMetric());
			values.add(row.getValue());

			if (row.getMoreInfo() != null) {
				moreInfo.add(row.getMoreInfo());
			} else {
				moreInfo.add("");
			}
			fireTableDataChanged();
		}
	}

	public void deleteRow(int index) {
		if(metrics.size() <= index){
			metrics.remove(index);
			values.remove(index);
			moreInfo.remove(index);
			fireTableDataChanged();
		}
	}
	
	public void clearTable(){
		metrics.clear();
		values.clear();
		moreInfo.clear();
	}
	

	public boolean isCellEditable(int r, int c) {
		return false;
	}
}
