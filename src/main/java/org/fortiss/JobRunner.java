/**
 * 
 */
package org.fortiss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.bean.WorkflowConfFields;

import com.google.common.collect.ImmutableList;

/**
 * @author rajat
 *
 */
public class JobRunner {

	public static final Logger logger = LogManager.getLogger(JobRunner.class);
	private static JobRunner jobRunner = null;
	private static ImmutableList<String> toolsStatus = ImmutableList
			.<String> builder().add("available").add("unavailable").build();

	private JobRunner() {
	}

	public static JobRunner getInstance() {
		if (jobRunner == null) {
			synchronized (JobRunner.class) {
				if (jobRunner == null) {
					jobRunner = new JobRunner();
				}
			}
		}
		return jobRunner;
	}

	public void runWorkflow(String wfId, File confFilePath) throws IOException {
		FileReader fileReader = new FileReader(confFilePath);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		ArrayList<String> fieldValuesList = new ArrayList<String>();
		String line = bufferedReader.readLine();
		int arrayLength = 0;
		while (line != null
				&& arrayLength <= WorkflowConfFields.values().length) {
			System.out.println(line);
			fieldValuesList.add(line);
			line = bufferedReader.readLine();
			arrayLength++;
		}
		bufferedReader.close();
		String[] fieldValues = fieldValuesList
				.toArray(new String[fieldValuesList.size()]);
		runWorkflow(wfId, fieldValues);
	}

	public void runWorkflow(String wfId, String conf)
			throws NumberFormatException, IOException {
		String[] fieldValues = conf.split(",");
		runWorkflow(wfId, fieldValues);
	}

	/**
	 * @param wfId
	 * @param fieldValues
	 * @throws IOException
	 */
	private void runWorkflow(String wfId, String[] fieldValues)
			throws IOException {
		Map<WorkflowConfFields, String> confMap = parseConf(fieldValues);
		if (verifyWorkflow(wfId, confMap)) {
			System.out
					.println("All configuration verified \nTODO : Verify tools now..");
			Map<WorkflowConfFields, Map<String, ArrayList<String>>> mapTools = getToolsStatus(confMap);
			System.out.println("All tools checked");
			for (Entry<WorkflowConfFields, Map<String, ArrayList<String>>> fieldEntry : mapTools
					.entrySet()) {
				System.out.println("Tools Status : "
						+ fieldEntry.getKey().name());
				for (Entry<String, ArrayList<String>> entry : fieldEntry
						.getValue().entrySet()) {
					System.out.println(entry.getKey() + " # "
							+ entry.getValue());
				}
				final ArrayList<String> availableTools = fieldEntry.getValue()
						.get(toolsStatus.get(0));
				Thread thread = new Thread(new Runnable() {

					public void run() {
						System.out.println("Sending tools to jenkins to run # "
								+ availableTools.size());

					}

				});

				thread.start();
			}

			// TODO : Verify tools exist and then send them to jenkins to run
		}
	}

	private Map<WorkflowConfFields, Map<String, ArrayList<String>>> getToolsStatus(
			Map<WorkflowConfFields, String> confMap) throws IOException {
		List<ToolBean> allData = DataProcessor.getInstance().getToolData();
		List<String> listOfAddedTools = new ArrayList<String>();
		for (ToolBean bean : allData) {
			listOfAddedTools.add(bean.getName());
		}
		Map<WorkflowConfFields, Map<String, ArrayList<String>>> totalMap = new HashMap<WorkflowConfFields, Map<String, ArrayList<String>>>();
		for (Entry<WorkflowConfFields, String> entry : confMap.entrySet()) {
			WorkflowConfFields confField = entry.getKey();
			Map<String, ArrayList<String>> availableToolsMap = new HashMap<String, ArrayList<String>>();
			availableToolsMap.put(toolsStatus.get(0), new ArrayList<String>());
			availableToolsMap.put(toolsStatus.get(1), new ArrayList<String>());
			totalMap.put(confField, availableToolsMap);
			if ("".equals(entry.getValue())) {
				continue;
			}
			String[] tools = entry.getValue().split(" ");
			for (int i = 0; i < tools.length; i++) {
				String toolName = tools[i];
				if (listOfAddedTools.contains(toolName)) {
					String testScript = getTestScript(toolName, allData);
					String output = executeCommand(testScript);
					if (output.contains("not found")) {
						System.out.println("Tool not installed : " + toolName);
						availableToolsMap.get(toolsStatus.get(1)).add(toolName);
					} else {
						System.out.println("Tool installed : " + toolName);
						availableToolsMap.get(toolsStatus.get(0)).add(toolName);
					}
				} else {
					System.out.println("Tool not added to database : "
							+ toolName);
					availableToolsMap.get(toolsStatus.get(1)).add(toolName);
				}
			}
		}
		return totalMap;
	}

	private String getTestScript(String toolName, List<ToolBean> allData) {
		String defaultTestScript = toolName.toLowerCase() + " --help";
		String testScript = defaultTestScript;
		for (ToolBean bean : allData) {
			if (bean.getName().equalsIgnoreCase(toolName)) {
				testScript = bean.getTestScript();
				if (testScript.isEmpty()) {
					testScript = defaultTestScript;
				}
			}
		}
		return testScript;
	}

	private String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			output.append("command not found");
		}
		return output.toString();
	}

	/**
	 * 
	 * @param conf
	 *            eg. 'icpc cppcheck','model_check','' where the first entry is
	 *            static analyzers, then model checkers, then wpc
	 * @return
	 */
	private Map<WorkflowConfFields, String> parseConf(String[] fieldValues) {
		Map<WorkflowConfFields, String> mapConfFields = new HashMap<WorkflowConfFields, String>();
		ArrayList<String> fieldValuesList = new ArrayList<String>(
				Arrays.asList(fieldValues));
		while (fieldValuesList.size() < WorkflowConfFields.values().length) {
			fieldValuesList.add("");
		}
		if (fieldValuesList.size() > 3) {
			System.out
					.println("Extra information provided \nPlease check the configuration again");
			return null;
		}
		mapConfFields.put(WorkflowConfFields.StaticAnalyzers,
				fieldValuesList.get(0));
		mapConfFields.put(WorkflowConfFields.ModelCheckers,
				fieldValuesList.get(1));
		mapConfFields.put(WorkflowConfFields.WPreCondition,
				fieldValuesList.get(2));
		return mapConfFields;
	}

	private boolean verifyWorkflow(String wfId,
			Map<WorkflowConfFields, String> confDetails)
			throws NumberFormatException, IOException {
		WorkflowBean data = DataProcessor.getInstance().getWorkflowData(
				Integer.parseInt(wfId));
		Map<WorkflowConfFields, String> persistentConfMap = data
				.getConfDetails();
		for (Entry<WorkflowConfFields, String> entry : persistentConfMap
				.entrySet()) {
			String persistentVal = entry.getValue();
			String confVal = confDetails.get(entry.getKey());

			int numberOfProvidedTools = confVal.split(" ").length;
			if ("".equals(confVal)) {
				numberOfProvidedTools = 0;
			}

			int numberOfTools = -1;
			if (persistentVal.startsWith(">")) {
				numberOfTools = Integer.parseInt(persistentVal.substring(1));
				if (numberOfProvidedTools <= numberOfTools) {
					System.out
							.println("Insufficient number of tools provided for "
									+ entry.getKey().name());
					return false;
				}

			} else if (persistentVal.startsWith("<")) {
				numberOfTools = Integer.parseInt(persistentVal.substring(1));
				if (numberOfProvidedTools >= numberOfTools) {
					System.out.println("Extra number of tools provided for "
							+ entry.getKey().name());
					return false;
				}
			} else if (persistentVal.equals("any")) {
				if (numberOfProvidedTools < 0) {
					System.out.println("Add an entry in the configuration for "
							+ entry.getKey().name());
					return false;
				}
			} else {
				numberOfTools = Integer.parseInt(persistentVal);
				if (numberOfProvidedTools != numberOfTools) {
					System.out.println("Invalid number of tools provided for "
							+ entry.getKey().name());
					return false;
				}
			}
		}

		return true;
	}
}
