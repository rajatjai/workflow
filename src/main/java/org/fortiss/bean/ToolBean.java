/**
 * 
 */
package org.fortiss.bean;

import java.io.Serializable;

/**
 * @author rajat
 *
 */
public class ToolBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8035922004653207624L;
	private int id;
	private String name;
	private String type;
	private String runScript;
	private String testScript;

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the runScript
	 */
	public String getRunScript() {
		return runScript;
	}

	/**
	 * @param runScript
	 *            the runScript to set
	 */
	public void setRunScript(String runScript) {
		this.runScript = runScript;
	}

	/**
	 * @return the testScript
	 */
	public String getTestScript() {
		return testScript;
	}

	/**
	 * @param testScript
	 *            the testScript to set
	 */
	public void setTestScript(String testScript) {
		this.testScript = testScript;
	}

}
