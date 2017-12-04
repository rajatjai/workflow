/**
 * 
 */
package org.fortiss;

import java.util.List;
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

import com.google.common.collect.ImmutableList;

/**
 * @author rajat
 *
 */
public class Foo {

	public static final Logger logger = LogManager.getLogger(Foo.class);
	public static List<String> workflowFields = ImmutableList
			.<String> builder().add("<").add(">").add("any").build();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		options.addOption(Option.builder().required(false).longOpt("help")
				.desc("Print Usage").build());
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				Scanner sc = new Scanner(System.in);
				boolean invalidEntries = true;
				for (int i = 0; i < 5; i++) {
					System.out.println("Enter number of ModelCheckers : #");
					String mc = sc.nextLine();
					System.out.println("Enter number of StaticAnalyzers : #");
					String sa = sc.nextLine();
					System.out.println("Enter number of WPC : #");
					String wpc = sc.nextLine();
					if (isValidEntry(mc) && isValidEntry(sa)
							&& isValidEntry(wpc)) {
						System.out.println("MC# " + mc + "\nSA# " + sa
								+ "\nWPC# " + wpc);
						sc.close();
						invalidEntries = false;
						break;
					} else {
						System.out.println("Invalid Entry\nPlease try again");
					}
				}
				if (invalidEntries) {
					System.out.println("No more chance for you!\nCiao.");
					sc.close();
				}
			}
		} catch (ParseException e) {
			logger.error("Exception while parsing.");
			printHelp(options, formatter);
			System.exit(-1);
		}
	}

	private static boolean isValidEntry(String entry) {
		String entryNumStr = "";
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
				+ "--addtool <name>,<type>,[<fp/fn>],<test-script>,<run-script>\n"
				+ "--addwf <name>,<desc>,<detail1:detail2:detail3>\n"
				+ "--tools\n" + "--wf\n" + "--rmtool 0\n" + "--rmwf 1\n";
		String header = "";
		formatter.printHelp("javac RunService", header, options, footer);
	}

}
