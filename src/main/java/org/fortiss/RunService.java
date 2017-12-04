/**
 * 
 */
package org.fortiss;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.bean.WorkflowConfFields;

/**
 * @author rajat
 *
 */
public class RunService {

	public final static Logger logger = LogManager.getLogger(RunService.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Map<String, String> argMap = getArgumentsFromUI();
		logger.info("Starting..");
		// ParseCLI.getConfig(args);
		logger.info("Finished.");

	}

	private static Map<String, String> getArgumentsFromUI() throws IOException {
		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);
		String todoArr[] = { "Add Workflow", "Add Tool", "List all Workflows",
				"List all Tools", "Remove Worklow", "Remove Tool",
				"Run Workflow" };
		JComboBox<String> comboBox = new JComboBox<String>(todoArr);
		comboBox.setSelectedIndex(0);
		JPanel myPanel = new JPanel();
		// myPanel.add(new JLabel("x:"));
		// myPanel.add(xField);
		// myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		// myPanel.add(new JLabel("y:"));
		// myPanel.add(yField);
		myPanel.add(comboBox);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"What do you want to do?", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int index = comboBox.getSelectedIndex();
			JPanel panelUI = new JPanel();
			switch (index) {
			case 0:
				createAddWfUI(panelUI);
				break;
			case 1:
				createAddToolUI(panelUI);
				break;
			case 2:
				showAllWfsUI(panelUI);
				break;
			case 3:
				showAllToolsUI(panelUI);
				break;
			case 4:
				createRemoveWfUI(panelUI);
				break;
			case 5:
				createRemoveToolUI(panelUI);
				break;
			case 6:
				createRunWfUI(panelUI);
				break;
			default:
				break;
			}
			// System.out.println("x value: " + xField.getText());
			// System.out.println("y value: " + yField.getText());
		}
		return null;
	}

	private static void createRunWfUI(JPanel panelUI) {
		int result = JOptionPane.showConfirmDialog(null, panelUI,
				"Set parameters to run workflow", JOptionPane.OK_CANCEL_OPTION);
	}

	private static void createRemoveToolUI(JPanel panelUI) throws IOException {
		JComboBox<String> typeComboBox = new JComboBox<String>();
		DataProcessor dataProcessor = DataProcessor.getInstance();
		List<ToolBean> res = dataProcessor.getToolData();
		List<String> toolsArray = new ArrayList<String>();
		for (ToolBean tool : res) {
			toolsArray.add(tool.getName());
			typeComboBox.addItem(tool.getName());
		}
		typeComboBox.setSelectedIndex(0);
		panelUI.add(new JLabel("Tool:"));
		panelUI.add(typeComboBox);
		int result = JOptionPane.showConfirmDialog(null, panelUI,
				"Select Tool to remove", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		} else if (result == JOptionPane.OK_OPTION) {
			dataProcessor.deleteData(typeComboBox.getSelectedIndex(),
					ToolBean.class);
			JOptionPane.showMessageDialog(panelUI,
					"Tool: " + typeComboBox.getSelectedItem() + " deleted!");
		}
	}

	private static void createRemoveWfUI(JPanel panelUI) throws IOException {
		JComboBox<String> wfComboBox = new JComboBox<String>();
		DataProcessor dataProcessor = DataProcessor.getInstance();
		List<WorkflowBean> res = dataProcessor.getWorkflowData();
		List<String> wfsList = new ArrayList<String>();
		for (WorkflowBean wf : res) {
			wfsList.add(wf.getName());
			wfComboBox.addItem(wf.getName());
		}
		wfComboBox.setSelectedIndex(0);
		panelUI.add(new JLabel("Workflow:"));
		panelUI.add(wfComboBox);
		int result = JOptionPane.showConfirmDialog(null, panelUI,
				"Select Workflow to remove", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		} else if (result == JOptionPane.OK_OPTION) {
			// dataProcessor.deleteData(wfComboBox.getSelectedIndex(),
			// WorkflowBean.class);
			JOptionPane.showMessageDialog(panelUI,
					"Workflow: " + wfComboBox.getSelectedItem() + " deleted!");
		}

	}

	private static void showAllToolsUI(JPanel panelUI) throws IOException {
		DataProcessor dataProcessor = DataProcessor.getInstance();
		List<ToolBean> res = dataProcessor.getToolData();
		String dialog = "All available tools:\n";
		for (ToolBean tool : res) {
			dialog += tool.getId() + " # " + tool.getName() + "\n";
		}
		logger.info(dialog);
		int result = JOptionPane.showConfirmDialog(panelUI, dialog, "Tools",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		}
	}

	private static void showAllWfsUI(JPanel panelUI) throws IOException {
		DataProcessor dataProcessor = DataProcessor.getInstance();
		List<WorkflowBean> res = dataProcessor.getWorkflowData();
		String dialog = "All available workflows:\n";
		for (WorkflowBean wf : res) {
			dialog += wf.getId() + " # " + wf.getName() + "\n";
		}
		logger.info(dialog);
		int result = JOptionPane.showConfirmDialog(panelUI, dialog,
				"Workflows", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		}
	}

	private static void createAddToolUI(JPanel panelUI) throws IOException {
		JTextField name = new JTextField(0);
		String typeArr[] = { "Static Analyzer", "Model Checker", "WP" };
		JComboBox<String> typeComboBox = new JComboBox<String>(typeArr);
		typeComboBox.setSelectedIndex(0);
		JTextField runscript = new JTextField(0);
		JTextField testscript = new JTextField(0);
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(4, 2, 2, 5));
		myPanel.add(new JLabel("Name:"));
		myPanel.add(name);
		myPanel.add(new JLabel("Type:"));
		myPanel.add(typeComboBox);
		myPanel.add(new JLabel("Run Script:"));
		myPanel.add(runscript);
		myPanel.add(new JLabel("Test Script (Optional):"));
		myPanel.add(testscript);
		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Set parameters to add tool", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		} else if (result == JOptionPane.OK_OPTION) {
			String dataArr[] = new String[4];
			if (StringUtils.isNotEmpty(name.getText())) {
				dataArr[0] = name.getText();
				dataArr[1] = typeArr[typeComboBox.getSelectedIndex()];
				if (StringUtils.isNotEmpty(runscript.getText())) {
					dataArr[2] = runscript.getText();
					if (StringUtils.isNotEmpty(testscript.getText())) {
						dataArr[3] = testscript.getText();
					}
					DataProcessor dataProcessor = DataProcessor.getInstance();
					dataProcessor.addData(dataArr, ToolBean.class);
					JOptionPane.showMessageDialog(panelUI, "Tool: "
							+ dataArr[0] + " added!");
				} else {
					logger.error("Empty runscript field");
					JOptionPane.showMessageDialog(myPanel,
							"Run Script field cannot be empty!");
				}
			} else {
				logger.error("Empty name field");
				JOptionPane.showMessageDialog(myPanel,
						"Name field cannot be empty!");
			}
		}
	}

	private static void createAddWfUI(JPanel panelUI) throws IOException {

		JTextField name = new JTextField(0);
		JTextField description = new JTextField(0);
		// String saArr[] = { "0", "1", "1+" };
		// JComboBox<String> saComboBox = new JComboBox<String>(saArr);
		// saComboBox.setSelectedIndex(0);
		// String mcArr[] = { "0", "1", "1+" };
		// JComboBox<String> mcComboBox = new JComboBox<String>(mcArr);
		// mcComboBox.setSelectedIndex(0);
		// String wpArr[] = { "0", "1", "1+" };
		// JComboBox<String> wpComboBox = new JComboBox<String>(wpArr);
		// wpComboBox.setSelectedIndex(0);

		// New signature method

		String presenceArr[] = { "Mandatory", "Optional" };
		String countArr[] = { "Single", "Multiple" };

		JComboBox<String> saComboBoxPresence = new JComboBox<String>(
				presenceArr);
		JComboBox<String> mcComboBoxPresence = new JComboBox<String>(
				presenceArr);
		JComboBox<String> wpComboBoxPresence = new JComboBox<String>(
				presenceArr);
		wpComboBoxPresence.setSelectedIndex(1);
		JComboBox<String> saComboBoxCount = new JComboBox<String>(countArr);
		saComboBoxCount.setSelectedIndex(1);
		JComboBox<String> mcComboBoxCount = new JComboBox<String>(countArr);
		JComboBox<String> wpComboBoxCount = new JComboBox<String>(countArr);
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(6, 3, 2, 5));
		myPanel.add(new JLabel("Name:"));
		myPanel.add(name);
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Description:"));
		myPanel.add(description);
		myPanel.add(new JLabel(""));
		myPanel.add(new JLabel("Configuration:"));
		myPanel.add(new JLabel("Presence"));
		myPanel.add(new JLabel("Count"));
		myPanel.add(new JLabel("Static Analyzers"), JOptionPane.RIGHT_ALIGNMENT);
		myPanel.add(saComboBoxPresence);
		myPanel.add(saComboBoxCount);
		myPanel.add(new JLabel("Model Checkers"));
		myPanel.add(mcComboBoxPresence);
		myPanel.add(mcComboBoxCount);
		myPanel.add(new JLabel("WP"));
		myPanel.add(wpComboBoxPresence);
		myPanel.add(wpComboBoxCount);

		// JPanel myPanel = new JPanel();
		// myPanel.setLayout(new GridLayout(5, 3, 2, 5));
		// myPanel.add(new JLabel("Name:"));
		// myPanel.add(name);
		// myPanel.add(new JLabel(""));
		// myPanel.add(new JLabel("Description:"));
		// myPanel.add(description);
		// myPanel.add(new JLabel(""));
		// myPanel.add(new JLabel("Configuration:"));
		// myPanel.add(new JLabel("Static Analyzers"));
		// myPanel.add(saComboBox);
		// myPanel.add(new JLabel(""));
		// myPanel.add(new JLabel("Model Checkers"));
		// myPanel.add(mcComboBox);
		// myPanel.add(new JLabel(""));
		// myPanel.add(new JLabel("WP"));
		// myPanel.add(wpComboBox);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Set parameters to add workflow", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.CANCEL_OPTION) {
			getArgumentsFromUI();
		} else if (result == JOptionPane.OK_OPTION) {
			String wfName = name.getText();
			String wfDesc = description.getText();
			// SA
			String saPresence = (String) saComboBoxPresence.getSelectedItem();
			String mcPresence = (String) mcComboBoxPresence.getSelectedItem();
			String wpPresence = (String) wpComboBoxPresence.getSelectedItem();
			String saCount = (String) saComboBoxCount.getSelectedItem();
			String mcCount = (String) mcComboBoxCount.getSelectedItem();
			String wpCount = (String) wpComboBoxCount.getSelectedItem();
			Map<String, String> wfMap = new HashMap<String, String>();
			wfMap.put("name", wfName);
			wfMap.put("description", wfDesc);
			wfMap.put(WorkflowConfFields.StaticAnalyzers.toString(), saPresence
					+ ", " + saCount);
			wfMap.put(WorkflowConfFields.ModelCheckers.toString(), mcPresence
					+ ", " + mcCount);
			wfMap.put(WorkflowConfFields.WPreCondition.toString(), wpPresence
					+ ", " + wpCount);
			logger.info("Adding workflow with details : \n" + wfMap);
		}
	}
}
