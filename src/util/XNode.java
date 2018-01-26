package util;

import java.util.List;

public class XNode {
	private int[] metrics;
	private int attributes;
	private String parentID;
	private String id;
	private List<String> childrenIDs;
	
	public XNode(){
		
	}

	public int getAttributes() {
		return attributes;
	}
	
	
	/**
	 * 
	 * @param index 0 based int for the possition of the bit to check.
	 * @return true if it has the attribute or false otherwise.
	 */
	public boolean hasAttribute(int index) {
		int tmp = attributes;	/* e.g tmp = 1001 1011 , index = 5 */
		tmp = tmp >> index;		/* Shift right by 5 places - 1001 1011 -> 0000 0010 */
		tmp = tmp & 1;			/* AND the result with 0000 0001 which will leave only the desired bit as it is - 0000 0010 -> 0000 0000*/
		return tmp == 1;		/* If the bit was 1, it will stay 1 - true, otherwise it will become 0 - false */
	}

	public void setAttributes(int attributes) {
		this.attributes = attributes;
	}

	/***
	 * 
	 * @param index 0 based int for the possition of the bit to set.
	 * @param hasAttribute the boolean value (1 = true, 0 = false)
	 */
	public void setAttribute(int index, boolean hasAttribute) {
		if(index > 7)
			return;
		
		int tmp = 1; // 0000 0001
		for(int i = 0; i < index; i++){
			tmp = tmp << 1; // shift left by 1 index number of times eg. 5 -> 0010 0000
		}
		
		attributes = attributes & tmp;
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
	
	public void addChidrenId(String childId){
		childrenIDs.add(childId);
	}

}
