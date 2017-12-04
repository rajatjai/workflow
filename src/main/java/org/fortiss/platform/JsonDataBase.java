/**
 * 
 */
package org.fortiss.platform;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.DBRecordItems;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.platform.api.IDataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author rajat
 *
 */
public class JsonDataBase implements IDataBase {

	public static final Logger logger = LogManager
			.getLogger(JsonDataBase.class);

	private Writer writer;
	private Reader reader;
	private Gson gson;
	Map<String, List<String>> JSON_MAP_DUMMY = new HashMap<String, List<String>>();

	private List<String> keyList = new ArrayList<String>();
	private static final String JSON_DB_PATH = "/Users/rajat/work/workspace/head/fortiss/src/main/resources/db.json";

	// public Type type = new TypeToken<Map<String, List<String>>>() {
	// }.getType();

	@SuppressWarnings("unchecked")
	public JsonDataBase() throws IOException {

		reader = new FileReader(JSON_DB_PATH);
		gson = new GsonBuilder().create();
		Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
				JSON_MAP_DUMMY.getClass());
		keyList.add(DBRecordItems.TOOLS.name().toLowerCase());
		keyList.add(DBRecordItems.WORKFLOWS.name().toLowerCase());
		Map<String, List<String>> jsonMap = new HashMap<String, List<String>>();
		if (jsonDataMap == null || jsonDataMap.isEmpty()) {
			writer = new FileWriter(JSON_DB_PATH);
			jsonMap.put(keyList.get(0), new ArrayList<String>());
			jsonMap.put(keyList.get(1), new ArrayList<String>());
			gson.toJson(jsonMap, writer);
			writer.close();
		}

	}

	@SuppressWarnings("unchecked")
	public <T> Integer createRecord(T record) throws IOException {
		try {

			reader = new FileReader(JSON_DB_PATH);
			Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
					JSON_MAP_DUMMY.getClass());
			writer = new FileWriter(JSON_DB_PATH);
			if (record instanceof ToolBean) {
				List<String> toolList = jsonDataMap.get(keyList.get(0));
				toolList.add(gson.toJson(record, ToolBean.class));
				gson.toJson(jsonDataMap, writer);
				return toolList.size();
			} else if (record instanceof WorkflowBean) {
				List<String> wfList = jsonDataMap.get(keyList.get(1));
				wfList.add(gson.toJson(record, WorkflowBean.class));
				gson.toJson(jsonDataMap, writer);
				return wfList.size();
			}
			return -1;
		} finally {
			writer.close();
			reader.close();
		}
	}

	public <T> Integer updateRecord(Integer recordId, T record) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getRecord(Integer recordId, T clazz) throws IOException {
		try {

			reader = new FileReader(JSON_DB_PATH);
			Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
					JSON_MAP_DUMMY.getClass());
			String record;
			if (clazz instanceof ToolBean) {
				List<String> toolList = jsonDataMap.get(keyList.get(0));
				record = toolList.get(recordId);
				ToolBean bean = gson.fromJson(record, ToolBean.class);
				bean.setId(recordId);
				return (T) bean;
			} else if (clazz instanceof WorkflowBean) {
				List<String> wfList = jsonDataMap.get(keyList.get(1));
				record = wfList.get(recordId);
				WorkflowBean bean = gson.fromJson(record, WorkflowBean.class);
				bean.setId(recordId);
				return (T) bean;
			}
			return null;
		} finally {
			reader.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getRecords(T clazz) throws IOException {
		try {

			reader = new FileReader(JSON_DB_PATH);
			Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
					JSON_MAP_DUMMY.getClass());
			List<T> recordList = new ArrayList<T>();
			if (clazz instanceof ToolBean) {
				List<String> toolList = jsonDataMap.get(keyList.get(0));
				for (int i = 0; i < toolList.size(); i++) {
					ToolBean bean = gson.fromJson(toolList.get(i),
							ToolBean.class);
					bean.setId(i);
					recordList.add((T) bean);
				}
			} else if (clazz instanceof WorkflowBean) {
				List<String> wfList = jsonDataMap.get(keyList.get(1));
				for (int i = 0; i < wfList.size(); i++) {
					WorkflowBean bean = gson.fromJson(wfList.get(i),
							WorkflowBean.class);
					bean.setId(i);
					recordList.add((T) bean);
				}
			}
			return recordList;
		} finally {
			reader.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> boolean deleteRecord(Integer recordId, T clazz)
			throws IOException {
		boolean isDeleted = false;
		try {
			reader = new FileReader(JSON_DB_PATH);
			Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
					JSON_MAP_DUMMY.getClass());
			writer = new FileWriter(JSON_DB_PATH);
			if (clazz instanceof ToolBean) {
				List<String> toolList = jsonDataMap.get(keyList.get(0));
				if (recordId < toolList.size()) {
					toolList.remove(recordId.intValue());
					isDeleted = true;
				} else {
					logger.error("The provided tool id = {} does not exist",
							recordId);
					isDeleted = false;
				}
			} else if (clazz instanceof WorkflowBean) {
				List<String> wfList = jsonDataMap.get(keyList.get(1));
				if (recordId < wfList.size()) {
					wfList.remove(recordId.intValue());
					isDeleted = true;
				} else {
					logger.error(
							"The provided workflow id = {} does not exist",
							recordId);
					isDeleted = false;
				}
			}
			gson.toJson(jsonDataMap, writer);
			return isDeleted;
		} finally {
			writer.close();
			reader.close();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void deleteRecords(List<Integer> listRecordIds, T clazz)
			throws IOException {
		try {
			reader = new FileReader(JSON_DB_PATH);
			Map<String, List<String>> jsonDataMap = gson.fromJson(reader,
					JSON_MAP_DUMMY.getClass());
			writer = new FileWriter(JSON_DB_PATH);
			if (clazz instanceof ToolBean) {
				List<String> toolList = jsonDataMap.get(keyList.get(0));
				for (Integer recordId : listRecordIds) {
					if (recordId < toolList.size()) {
						toolList.remove(recordId.intValue());
					} else {
						logger.error(
								"The provided tool id = {} does not exist",
								recordId);
					}
				}
			} else if (clazz instanceof WorkflowBean) {
				List<String> wfList = jsonDataMap.get(keyList.get(1));
				for (Integer recordId : listRecordIds) {
					if (recordId < wfList.size()) {
						wfList.remove(recordId.intValue());
					} else {
						logger.error(
								"The provided workflow id = {} does not exist",
								recordId);
					}
				}
			}
			gson.toJson(jsonDataMap, writer);
		} catch (IndexOutOfBoundsException ex) {
			logger.error("The provided set of ids = {} is incorrect",
					listRecordIds);
		} finally {
			writer.close();
			reader.close();
		}
	}

}
