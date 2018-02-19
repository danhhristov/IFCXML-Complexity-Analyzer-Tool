package util;

import java.util.List;

public class XNode {
	private int id;
	private String name;
	private List<String> childrenIDs;
	private List<String> refferences;
	
	public XNode(String name){
		this.name = name.intern();
	}
	
	public String getName(){
		return this.name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public List<String> getRefferences() {
		return refferences;
	}

	public void setRefferences(List<String> refferences) {
		this.refferences = refferences;
	}
	
	public void addRefference(String ref){
		this.refferences.add(ref);
	}

}
