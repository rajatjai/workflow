/**
 * 
 */
package org.fortiss.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fortiss.DataProcessor;
import org.fortiss.JobRunner;
import org.fortiss.bean.ToolBean;
import org.fortiss.bean.WorkflowBean;
import org.fortiss.bean.WorkflowConfFields;

import com.google.gson.Gson;

/**
 * @author rajat
 *
 */
public class ParseCLI {
	public static final Logger logger = LogManager.getLogger(ParseCLI.class);

	public static Gson getConfig(String args[]) throws IOException {

		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		DataProcessor dataProcessor = DataProcessor.getInstance();
		JobRunner jobRunner = JobRunner.getInstance();
		options.addOption(Option.builder().required(false).longOpt("help")
				.desc("Print Usage").build());

		options.addOption(Option.builder().required(false).longOpt("tools")
				.hasArg(false).desc("List tools").build());

		options.addOption(Option.builder().required(false).longOpt("wf")
				.desc("List wfs").build());

		options.addOption(Option.builder().required(false).longOpt("addtool")
				.hasArg().desc("Add tool").build());

		options.addOption(Option.builder().required(false).longOpt("addwf")
				.desc("Add workflow").build());

		options.addOption(Option.builder().required(false).longOpt("rmtool")
				.hasArg().desc("Remove tool").build());

		options.addOption(Option.builder().required(false).longOpt("rmwf")
				.hasArg().desc("Remove workflow").build());

		options.addOption(Option.builder().required(false).longOpt("getwf")
				.hasArg().desc("Get data for workflow").build());

		options.addOption(Option.builder().required(false).longOpt("gettool")
				.hasArg().desc("Get data for tool").build());

		options.addOption(Option.builder().required(false).longOpt("runwf")
				.hasArgs().desc("Run workflow").build());
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				printHelp(options, formatter);
				System.exit(0);
			}

			if (cmd.hasOption("tools")) {
				dataProcessor.getAllData(ToolBean.class);
			}

			if (cmd.hasOption("wf")) {
				dataProcessor.getAllData(WorkflowBean.class);
			}

			if (cmd.hasOption("addtool")) {
				String addToolCmd = cmd.getOptionValue("addtool");
				String[] toolList = addToolCmd.split(",");
				if (toolList.length < 3) {
					logger.error("Not enough arguments to add tool.");
					return null;
				} else {
					dataProcessor.addData(toolList, ToolBean.class);
				}

			}

			if (cmd.hasOption("addwf")) {

				Scanner sc = new Scanner(System.in);
				boolean invalidEntries = true;
				System.out.println("Enter name for the workflow : ");
				String wfName = sc.nextLine();
				System.out.println("Enter description for the workflow : ");
				String wfDesc = sc.nextLine();

				Map<WorkflowConfFields, String> confFields = new HashMap<WorkflowConfFields, String>();
				for (int i = 0; i < 3; i++) {
					System.out
							.println("Enter signature (eg. sa(m,m),mc(m,s),wp(o,m)) : #");
					String mc = sc.nextLine();

					if (isValidWfEntry(mc)) {
						// System.out.println("MC# " + mc + "\nSA# " + sa
						// + "\nWPC# " + wpc);
						// sc.close();
						invalidEntries = false;
						// confFields.put(WorkflowConfFields.StaticAnalyzers,
						// sa);
						// confFields.put(WorkflowConfFields.ModelCheckers, mc);
						// confFields.put(WorkflowConfFields.WPreCondition,
						// wpc);
						break;
					} else {
						System.out.println("Invalid Entry\nPlease try again");
					}
				}
				if (invalidEntries) {
					System.out.println("No more chance for you!\nCiao.");
					sc.close();
					System.exit(0);
				}

				dataProcessor.addWorkFlowData(wfName, wfDesc, confFields);
				// String addWfCmd = cmd.getOptionValue("addwf");
				// String[] wfList = addWfCmd.split(",");
				// if (wfList.length < 3) {
				// logger.error("Not enough arguments to add workflow.");
				// return null;
				// } else {
				// if (checkConfDetails(wfList[2])) {
				// dataProcessor.addData(wfList, WorkflowBean.class);
				// } else {
				// return null;
				// }
				// }

			}

			if (cmd.hasOption("rmtool")) {
				String toolIds = cmd.getOptionValue("rmtool");
				String[] toolIdsArr = toolIds.split(",");
				if (toolIdsArr == null || toolIdsArr.length == 0) {
					logger.error("Not enough arguments to remove tool.");
					return null;
				} else {
					for (String toolId : toolIdsArr) {
						dataProcessor.deleteData(Integer.valueOf(toolId),
								ToolBean.class);
					}
				}
			}

			if (cmd.hasOption("rmwf")) {

				String wfIds = cmd.getOptionValue("rmwf");
				String[] wfIdsArr = wfIds.split(",");
				if (wfIdsArr == null || wfIdsArr.length == 0) {
					logger.error("Not enough arguments to remove workflow.");
					return null;
				} else {
					for (String wfId : wfIdsArr) {
						dataProcessor.deleteData(Integer.valueOf(wfId),
								WorkflowBean.class);
					}
				}
			}

			if (cmd.hasOption("runwf")) {
				String[] wfData = cmd.getOptionValues("runwf");
				File confFile = new File(wfData[1]);
				if (confFile.exists()) {
					jobRunner.runWorkflow(wfData[0], confFile);
				} else {
					jobRunner.runWorkflow(wfData[0], wfData[1]);
				}
			}
		} catch (ParseException e) {
			logger.error("Exception while parsing.");
			printHelp(options, formatter);
			System.exit(-1);
		}

		return null;
	}

	private static boolean isValidWfEntry(String entry) {
		String entryNumStr = "";
		String[] signature = entry.split(")");
		if (signature.length > 0) {

		}
		if (entry.startsWith(">") || entry.startsWith("<")) {
			entryNumStr = entry.substring(1);
		} else if (entry.equals("any")) {
			return true;
		} else {
			entryNumStr = entry;
		}
		try {
			int num = Integer.parseInt(entryNumStr);
			if (num < 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static void printHelp(Options options, HelpFormatter formatter) {
		String footer = "\nExample:\n"
				+ "--addtool <name>,<type>,<run-script>,<OPTIONAL:test-script>\n"
				+ "--addwf <name>,<desc>,<conf-details>\n" + "--tools\n"
				+ "--wf\n" + "--rmtool 0\n" + "--rmwf 1\n"
				+ "--runwf <wfid> <file_path OR comma separated tools list>";
		String header = "";
		formatter.printHelp("javac RunService", header, options, footer);
	}
}
