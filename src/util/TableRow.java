package util;

/**
 * A custom helper class to help with the presentation of information via JTable
 * 
 * @author Daniel Hristov (2018)
 */
public class TableRow {

	private String metric = "";
	private String value = "";
	private String more_info = "";
	
	/**
	 * Getters and Setters for the properties of the TableRow Object
	 * 
	 */
	
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMoreInfo() {
		return more_info;
	}
	public void setMoreInfo(String more_info) {
		this.more_info = more_info;
	}
	
}
