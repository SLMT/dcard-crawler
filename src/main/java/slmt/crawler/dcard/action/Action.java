package slmt.crawler.dcard.action;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public abstract class Action {
	
	public static final String COMMAND_PREFIX = "java -jar dcard-crawler.jar ";
	
	protected Options options;
	
	public Action() {
		options = new Options();
		
		// Add common options
		options.addOption("v", "version", false, "show the version of this program");
	}
	
	protected CommandLine parse(String[] args) {
		// Parse arguments
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			printHelpThenExit(e.getLocalizedMessage());
		}
		
		return cmd;
	}
	
	public abstract void execute(String[] args);
	
	public abstract String getActionName();
	
	protected void printHelpThenExit(String errorMsg) {
		System.err.println("error: " + errorMsg);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(COMMAND_PREFIX +
				getActionName() + " [options]", options);
		System.exit(1);
	}
}
