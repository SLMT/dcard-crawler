package slmt.crawler.dcard.action;

import org.apache.commons.cli.CommandLine;

import slmt.crawler.dcard.DcardCrawler;

/**
 * 一個特殊的 Action，用來產生其他 Action 而生。
 * 
 * @author SLMT
 *
 */
public class TopAction extends Action {
	
	public TopAction() {
		options.addOption("v", "version", false, "show the version of this program");
	}
	
	@Override
	public void execute(String[] args) {
		CommandLine cmdLine = parse(args);
		
		// Show the version number
		if (cmdLine.hasOption("version")) {
			System.out.println("Current version: " + DcardCrawler.VERSION);
			System.exit(0);
		}
		
		// 根據指令產生對應 command 物件
		String input = cmdLine.getArgs()[0];
		Action cmd = null;
		if (input.equals(FetchPostAction.COMMAND_NAME))
			cmd = new FetchPostAction();
		
		if (cmd == null)
			printHelpThenExit("unrecognized command");
	}

	@Override
	public String getActionName() {
		return "";
	}
	
	@Override
	protected void printHelpThenExit(String errorMsg) {
		System.err.println("error: " + errorMsg);
		
		StringBuilder sb = new StringBuilder();
		sb.append("usage: " + COMMAND_PREFIX + "[action] [options] [arguments]\n");
		sb.append("here are the available actions:\n");
		sb.append("fetch-post: fetch posts from Dcard\n");
		sb.append("fetch-images: fetch images in the downloaded posts\n");
		
		sb.append("\n");
		sb.append("or, use '-v' to show the version number");
		
		System.out.println(sb.toString());
		System.exit(1);
	}
}
