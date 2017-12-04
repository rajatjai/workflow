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
import org.fortiss.bean.ToolBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author rajat
 *
 */
public class Test {

	public static final Logger logger = LogManager
			.getLogger(JsonToolchainManager.class);

	private static final String JSON_DB_PATH = "/Users/rajat/work/workspace/head/fortiss/src/main/resources/db2.json";

	public static <T> T getFromJson(Gson gson, String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public static <T> String toJson(Gson gson, T clazz) {
		return gson.toJson(clazz);
	}

	private Gson gson;

	public <T> T getFromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public <T> String toJson(T clazz) {
		return gson.toJson(clazz);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Writer writer = new FileWriter(JSON_DB_PATH);
		// Writer writer = new OutputStreamWriter(new FileOutputStream(
		// JSON_DB_PATH), "UTF-8");
		// Reader reader = new FileReader(JSON_DB_PATH);
		Gson gson = new GsonBuilder().create();

		Map<String, List<String>> jsonMap = new HashMap<String, List<String>>();

		List<String> keyList = new ArrayList<String>();

		keyList.add("tools");
		keyList.add("workflows");
		jsonMap.put(keyList.get(0), new ArrayList<String>());
		jsonMap.put(keyList.get(1), new ArrayList<String>());
		// String jsonToAdd = toJson(gson, jsonMap);
		// logger.info(jsonToAdd);
		// logger.info(getFromJson(gson, jsonToAdd, jsonMap.getClass()));

		// gson.toJson(jsonToAdd, writer);
		// writer.close();

		List<String> listTools = jsonMap.get(keyList.get(0));
		ToolBean toolBean = new ToolBean();
		toolBean.setName("icpc");
		toolBean.setType("model-checker");
		listTools.add(gson.toJson(toolBean, ToolBean.class));

		ToolBean toolBean1 = new ToolBean();
		toolBean1.setName("wmc");
		toolBean1.setType("model-validator");

		listTools.add(gson.toJson(toolBean1, ToolBean.class));

		// jsonToAdd = toJson(gson, jsonMap);
		// logger.info(jsonToAdd);
		// logger.info(getFromJson(gson, jsonToAdd, jsonMap.getClass()));
		Writer writer = new FileWriter(JSON_DB_PATH);
		gson.toJson(jsonMap, writer);
		writer.close();
		Reader reader = new FileReader(JSON_DB_PATH);
		// Reader reader = new InputStreamReader(
		// Test.class.getResourceAsStream(JSON_DB_PATH), "UTF-8");
		Map<String, List<String>> jsonMapNew = new HashMap<String, List<String>>();
		Map<String, List<String>> fromJsonMap = gson.fromJson(reader,
				jsonMapNew.getClass());
		for (String key : fromJsonMap.keySet()) {
			logger.info(key);
			logger.info(fromJsonMap.get(key));
		}
		List<String> toolData = fromJsonMap.get("tools");
		for (String element : toolData) {
			ToolBean bean = gson.fromJson(element, ToolBean.class);
			logger.info("{} : {}", bean.getId(), bean.getName());

		}

	}
}
