package slmt.crawler.dcard.action;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;
import slmt.crawler.dcard.downloader.DcardPostDownloader.Gender;

public class FetchPostAction extends Action {
	
	public static final String ACTION_NAME = "fetch-post";
	
	public FetchPostAction() {
		// create the Options
		options.addOption("e", "ex-no-img-posts", false, "do not download posts that don't have images");
		options.addOption("r", "redownload-exsiting", false, "re-download existing posts");
		options.addOption("d", "dont-count-redowns", false, "do not count re-downloaded posts");
		options.addOption(Option.builder("f").longOpt("forum")
				.hasArg().argName("forum alias").desc("specify a forum of Dcard").build());
		options.addOption(Option.builder("g").longOpt("gender")
				.hasArg().argName("M|F").desc("specify a gender (M=male, F=female)").build());
		options.addOption(Option.builder("b").longOpt("before-id")
				.hasArg().argName("post id").desc("download posts before the post with the given id").build());
	}
	
	@Override
	public void execute(String[] args) {
		CommandLine cmdLine = parse(args);
		
		// Retrieve the download path
		if (cmdLine.getArgs().length < 3)
			printHelpThenExit("please give more arguments");
		int numOfPosts = Integer.parseInt(cmdLine.getArgs()[1]);
		String downloadPath = cmdLine.getArgs()[2];
		
		// Create a post downloader
		DcardPostDownloader downloader = new DcardPostDownloader(downloadPath);
		
		// Set gender
		if (cmdLine.hasOption("gender")) {
			String g = cmdLine.getOptionValue("gender");
			if (g.equals("M"))
				downloader.setTargetGender(Gender.MALE);
			else if (g.equals("F"))
				downloader.setTargetGender(Gender.FEMALE);
			else
				printHelpThenExit("wrong gender value");
		}
		
		// Set forum
		if (cmdLine.hasOption("forum")) {
			String forumName = cmdLine.getOptionValue("forum");
			DcardForum forum = null;
			try {
				forum = DcardForum.getByName(forumName);
			} catch (IllegalArgumentException e) {
				printHelpThenExit("forum: " + forumName + " doesn't exist");
			}
			downloader.setTargetForum(forum);
		}
		
		// Set other options
		if (cmdLine.hasOption("ex-no-img-posts")) {
			downloader.onlyWithImage(true);
		}
		if (cmdLine.hasOption("redownload-exsiting")) {
			downloader.redownloadExistings(true);
		}
		if (cmdLine.hasOption("dont-count-redowns")) {
			downloader.dontCountRedownPosts(true);
		}
		
		// Download posts
		if (cmdLine.hasOption("before-id")) {
			int beforeId = Integer.parseInt(cmdLine.getOptionValue("before-id"));
			downloader.downloadPosts(numOfPosts, beforeId);
		} else
			downloader.downloadPosts(numOfPosts);
	}

	@Override
	public String getCommandSyntax() {
		return COMMAND_PREFIX + " " + ACTION_NAME + " [options] [number of posts] [download path]";
	}
	
}
