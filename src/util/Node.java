package util;

import java.util.List;

public class Node {
	private int[] metrics;
	private int attributes;
	private String parentID;
	private String id;
	private List<String> childrenIDs;
	
	public int getAttributes() {
		return attributes;
	}
	
	public boolean hasAttribute(int index){
		int tmp = attributes; /* e.g tmp = 1001 1011 , index = 5*/
		tmp = tmp >> index; /* Shift right by 5 places - 1001 1011 -> 0000 0010*/
		tmp = tmp & 0x01; /* AND the result with 0000 0001 which will leave only the desired bit as it is - 0000 0010 -> 0000 0000 */
		return tmp == 1; /* If the bit was 1, it will stay 1 - true, otherwise it will become 0 - false */
	}
	
	public void setAttributes(int attributes) {
		this.attributes = attributes;
	}
	
	public void setAttribute(int index, boolean hasAttribute){
		
	}
	
	public String getParentID() {
		return parentID;
	}
	
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getChildrenIDs() {
		return childrenIDs;
	}
	
	public void setChildrenIDs(List<String> childrenIDs) {
		this.childrenIDs = childrenIDs;
	}
	
}
