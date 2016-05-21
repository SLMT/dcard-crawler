package slmt.crawler.dcard.command;

import org.apache.commons.cli.CommandLine;

import slmt.crawler.dcard.DcardCrawler;

/**
 * 一個特殊的 Command，用來產生其他 Command 而生。
 * 
 * @author SLMT
 *
 */
public class TopCommand extends Command {
	
	public TopCommand() {
		options.addOption("v", "version", false, "show the version of this program");
	}
	
	@Override
	public void execute(String[] args) {
		CommandLine cmd = parse(args);
		
		// Show the version number
		if (cmd.hasOption("version")) {
			System.out.println("Current version: " + DcardCrawler.VERSION);
			System.exit(0);
		}
		
		// TODO: 根據指令產生對應 command 物件
	}

	@Override
	public String getActionName() {
		return "";
	}
	
	@Override
	protected void printHelpThenExit(String errorMsg) {
		System.err.println("error: " + errorMsg);
		
		StringBuilder sb = new StringBuilder();
		sb.append("usage: " + COMMAND_PREFIX + "[command] [options] [arguments]\n");
		sb.append("here are the available commands:\n");
		sb.append("fetch-post: fetch posts from Dcard\n");
		sb.append("fetch-images: fetch images in the downloaded posts\n");
		
		sb.append("\n");
		sb.append("or, use '-v' to show the version number");
		
		System.out.println(sb.toString());
		System.exit(1);
	}
}
