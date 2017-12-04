/**
 * 
 */
package org.fortiss;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.DataBase;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.bean.WorkflowConfFields;
import org.fortiss.platform.DBFactory;

import com.google.common.collect.ImmutableList;

/**
 * @author rajat
 *
 */
public class DataProcessor {

	public static final Logger logger = LogManager
			.getLogger(DataProcessor.class);
	private static DataProcessor dataProcessor = null;
	private DBFactory dbFactory = null;

	// public enum WfPresence {
	// Mandatory, Optional
	// }
	//
	// public enum WfCount {
	// Single, Multiple
	// }

	// public BiMap<String, String> wfFieldsMap = HashBiMap.create();
	//
	private DataProcessor() {
		dbFactory = DBFactory.getInstance();
		// wfFieldsMap.put(WfPresence.Mandatory.name(), "m");
		// wfFieldsMap.put(WfPresence.Optional.name(), "o");
		// wfFieldsMap.put(WfCount.Single.name(), "s");
		// wfFieldsMap.put(WfCount.Multiple.name(), "m");
	}

	public final ImmutableList<String> toolFields = ImmutableList
			.<String> builder().add("name").add("type").add("runScript")
			.build();
	public final ImmutableList<String> wfFields = ImmutableList
			.<String> builder().add("name").add("confDetails").build();

	public static DataProcessor getInstance() {
		if (dataProcessor == null) {
			synchronized (DataProcessor.class) {
				if (dataProcessor == null) {
					dataProcessor = new DataProcessor();
				}
			}
		}
		return dataProcessor;
	}

	public <T> int addData(String[] dataArr, T clazz) throws IOException {
		int id = -1;
		if (clazz.equals(ToolBean.class)) {
			ToolBean bean = new ToolBean();

			// Required Fields
			bean.setName(dataArr[0]);
			bean.setType(dataArr[1]);
			bean.setRunScript(dataArr[2]);

			// Optional Field
			bean.setTestScript(dataArr[3]);

			id = dbFactory.getToolchainManager(DataBase.JSON).setTool(bean);
			logger.info("Added tool with id={}", id);
		}
		// } else if (clazz.equals(WorkflowBean.class)) {
		// WorkflowBean bean = new WorkflowBean();
		//
		// // Required Fields
		// bean.setName(dataArr[0]);
		// String[] confList = dataArr[2].split(":");
		// for (int i = 0; i < confList.length; i++) {
		// bean.getConfDetails().put(i + "", confList[i]);
		// }
		//
		// // Optional Field
		// bean.setDesc(dataArr[1]);
		// id = dbFactory.getToolchainManager(DataBase.JSON).setWorkflow(bean);
		// logger.info("Added workflow with id={}", id);
		// }
		return id;
	}

	public int addWorkFlowData(String name, String desc,
			Map<WorkflowConfFields, String> confDetails) throws IOException {

		WorkflowBean bean = new WorkflowBean();
		bean.setName(name);
		bean.setDesc(desc);
		bean.setConfDetails(confDetails);
		int id = -1;
		id = dbFactory.getToolchainManager(DataBase.JSON).setWorkflow(bean);
		logger.info("Added workflow with id={}", id);
		return id;
	}

	// public String getSignatureString(Map<String, String> wfMap) {
	// StringBuilder signature = new StringBuilder();
	// signature.append("\\(");
	// String saConf = getPresenceCountShortened(wfMap
	// .get(WorkflowConfFields.StaticAnalyzers.toString()));
	// signature.append("sa" + saConf + ",");
	// String mcConf = getPresenceCountShortened(wfMap
	// .get(WorkflowConfFields.ModelCheckers.toString()));
	// signature.append("mc" + saConf + ",");
	// String wpConf = getPresenceCountShortened(wfMap
	// .get(WorkflowConfFields.WPreCondition.toString()));
	// signature.append("wp" + saConf + "\\)");
	// return signature.toString();
	// }

	// private String getPresenceCountShortened(String conf) {
	// // conf = Mandatory, Single
	// StringBuilder stringBuilder = new StringBuilder();
	// stringBuilder.append("\\(");
	// String[] split = conf.split(",");
	// if (split.length != 2) {
	// logger.error("Configuration is incorrect");
	// return null;
	// }
	// stringBuilder.append(wfFieldsMap.get(split[0]));
	// return null;
	// }

	public <T> int updateData(String[] dataArr, T clazz) {
		return -1;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAllData(Class<T> clazz) throws IOException {
		if (clazz.equals(ToolBean.class)) {
			List<ToolBean> toolBeanList = dbFactory.getToolchainManager(
					DataBase.JSON).getAllTools();
			for (ToolBean bean : toolBeanList) {
				logger.info("{} : {}", bean.getId(), bean.getName());
			}
			return (List<T>) toolBeanList;
		} else if (clazz.equals(WorkflowBean.class)) {
			List<WorkflowBean> wfBeanList = dbFactory.getToolchainManager(
					DataBase.JSON).getAllWokflows();
			for (WorkflowBean bean : wfBeanList) {
				logger.info("{} : {}", bean.getId(), bean.getName());
			}
			return (List<T>) wfBeanList;
		}
		return null;
	}

	public List<ToolBean> getToolData() throws IOException {
		List<ToolBean> toolBeanList = dbFactory.getToolchainManager(
				DataBase.JSON).getAllTools();
		for (ToolBean bean : toolBeanList) {
			logger.info("{} : {}", bean.getId(), bean.getName());
		}
		return toolBeanList;
	}

	public ToolBean getToolData(int id) throws IOException {
		ToolBean bean = dbFactory.getToolchainManager(DataBase.JSON)
				.getTool(id);

		logger.info("id : {}", bean.getId());
		logger.info("name : {}", bean.getName());
		logger.info("type : {}", bean.getType());
		logger.info("runScript : {}", bean.getRunScript());
		logger.info("testScript : {}", bean.getTestScript());
		return bean;

	}

	public List<WorkflowBean> getWorkflowData() throws IOException {
		List<WorkflowBean> wfBeanList = dbFactory.getToolchainManager(
				DataBase.JSON).getAllWokflows();
		for (WorkflowBean bean : wfBeanList) {
			logger.info("{} : {}", bean.getId(), bean.getName());
		}
		return wfBeanList;
	}

	public WorkflowBean getWorkflowData(int id) throws IOException {

		WorkflowBean bean = dbFactory.getToolchainManager(DataBase.JSON)
				.getWorkflow(id);
		logger.info("id : {}", bean.getId());
		logger.info("name : {}", bean.getName());
		logger.info("type : {}", bean.getDesc());
		logger.info("confDetails : {}", bean.getConfDetails());
		return bean;
	}

	public <T> boolean deleteAllData(T clazz) {
		return false;
	}

	public <T> boolean deleteData(int id, T clazz) throws IOException {
		if (clazz.equals(ToolBean.class)) {
			boolean isDeleted = dbFactory.getToolchainManager(DataBase.JSON)
					.deleteTool(id);
			logger.info("Tool id : {} deleted = {}", id, isDeleted);
			return isDeleted;
		} else if (clazz.equals(WorkflowBean.class)) {
			boolean isDeleted = dbFactory.getToolchainManager(DataBase.JSON)
					.deleteWorkflow(id);
			logger.info("Workflow id : {} deleted = {}", id, isDeleted);
			return isDeleted;
		}
		return false;
	}
}
