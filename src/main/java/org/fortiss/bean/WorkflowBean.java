/**
 * 
 */
package org.fortiss.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rajat
 *
 */
public class WorkflowBean implements Serializable {

	private static final long serialVersionUID = 1421858468890690086L;
	private int id;
	private String name;
	private String desc;
	private Map<WorkflowConfFields, String> confDetails = new HashMap<WorkflowConfFields, String>();

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the confDetails
	 */
	public Map<WorkflowConfFields, String> getConfDetails() {
		return confDetails;
	}

	/**
	 * @param confDetails
	 *            the confDetails to set
	 */
	public void setConfDetails(Map<WorkflowConfFields, String> confDetails) {
		this.confDetails = confDetails;
	}

}
