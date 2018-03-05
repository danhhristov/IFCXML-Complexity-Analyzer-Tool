package util;

import java.util.ArrayList;
import java.util.List;

/**
 *  XNOde is a custom helper class that aims to use tree-like structure
 *  to enable detailed analysis of each IFCXML element.
 * @author Daniel Hristov (2018)
 *
 */
public class XNode {
	private String id;
	private String attrId;
	private String name;
	private List<String> childrenIDs;
	private List<String> references;
	
	public XNode(String name){
		this.name = name.intern();
		childrenIDs = new ArrayList<String>();
		references = new ArrayList<String>();
	}
	
	
	/**
	 * Getters and Setters for the properties of the XNode Object
	 * 
	 */
	
	public String getName(){
		return this.name;
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

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> refferences) {
		this.references = refferences;
	}
	
	public void addReference(String ref){
		this.references.add(ref);
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

}
