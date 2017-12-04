/**
 * 
 */
package org.fortiss.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.DBRecordItems;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.platform.api.IToolchainManager;

import com.google.common.collect.ImmutableMap;

/**
 * @author rajat
 *
 */
public class JsonToolchainManager implements IToolchainManager {

	public static final Logger logger = LogManager
			.getLogger(JsonToolchainManager.class);

	private JsonDataBase jsonDataBase;
	private Map<DBRecordItems, Object> DUMMY_MAP = ImmutableMap
			.<DBRecordItems, Object> builder()
			.put(DBRecordItems.TOOLS, new ToolBean())
			.put(DBRecordItems.WORKFLOWS, new WorkflowBean()).build();

	public JsonToolchainManager() throws IOException {
		jsonDataBase = new JsonDataBase();
	}

	public int setTool(ToolBean toolBean) throws IOException {
		return jsonDataBase.createRecord(toolBean);
	}

	public int setWorkflow(WorkflowBean workflowBean) throws IOException {
		return jsonDataBase.createRecord(workflowBean);
	}

	public ToolBean getTool(int toolId) throws IOException {

		return (ToolBean) jsonDataBase.getRecord(toolId,
				DUMMY_MAP.get(DBRecordItems.TOOLS));
	}

	public WorkflowBean getWorkflow(int workflowId) throws IOException {
		return (WorkflowBean) jsonDataBase.getRecord(workflowId,
				DUMMY_MAP.get(DBRecordItems.WORKFLOWS));
	}

	public List<ToolBean> getAllTools() throws IOException {
		List<ToolBean> toolBeanList = new ArrayList<ToolBean>();
		List<Object> toolsList = jsonDataBase.getRecords(DUMMY_MAP
				.get(DBRecordItems.TOOLS));
		for (Object tool : toolsList) {
			toolBeanList.add((ToolBean) tool);
		}
		return toolBeanList;
	}

	public List<WorkflowBean> getAllWokflows() throws IOException {
		List<WorkflowBean> wfBeanList = new ArrayList<WorkflowBean>();
		List<Object> wfList = jsonDataBase.getRecords(DUMMY_MAP
				.get(DBRecordItems.WORKFLOWS));
		for (Object wf : wfList) {
			wfBeanList.add((WorkflowBean) wf);
		}
		return wfBeanList;
	}

	public boolean deleteTool(int toolId) throws IOException {
		return jsonDataBase.deleteRecord(toolId,
				DUMMY_MAP.get(DBRecordItems.TOOLS));
	}

	public boolean deleteWorkflow(int wfId) throws IOException {
		return jsonDataBase.deleteRecord(wfId,
				DUMMY_MAP.get(DBRecordItems.WORKFLOWS));
	}

	public int updateTool(int toolId, ToolBean toolBean) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateWorkflow(int wfId, WorkflowBean workflowBean)
			throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}
}