/**
 * 
 */
package org.fortiss.platform.api;

import java.io.IOException;
import java.util.List;

import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;

/**
 * @author rajat
 *
 */
public interface IToolchainManager {

	public int setTool(ToolBean toolBean) throws IOException;

	public int setWorkflow(WorkflowBean workflowBean) throws IOException;

	public int updateTool(int toolId, ToolBean toolBean) throws IOException;

	public int updateWorkflow(int wfId, WorkflowBean workflowBean)
			throws IOException;

	public ToolBean getTool(int toolId) throws IOException;

	public WorkflowBean getWorkflow(int workflowId) throws IOException;

	public List<ToolBean> getAllTools() throws IOException;

	public List<WorkflowBean> getAllWokflows() throws IOException;

	public boolean deleteTool(int toolId) throws IOException;

	public boolean deleteWorkflow(int wfId) throws IOException;
}
