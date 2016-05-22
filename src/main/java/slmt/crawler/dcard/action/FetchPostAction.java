package slmt.crawler.dcard.action;

import org.apache.commons.cli.Option;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;
import slmt.crawler.dcard.downloader.DcardPostDownloader.Gender;

public class FetchPostAction extends Action {
	
	public static final String ACTION_NAME = "fetch-post";
	
	public FetchPostAction() {
		// create the Options
		options.addOption("i", "download-images", false, "download images in the posts");
		options.addOption("e", "ex-no-img-posts", false, "do not download posts that don't have images");
		options.addOption(Option.builder("f").longOpt("forum")
				.hasArg().argName("forum alias").desc("specify a forum of Dcard").build());
		options.addOption(Option.builder("g").longOpt("gender")
				.hasArg().argName("M|F").desc("specify a gender (M=male, F=female)").build());
		options.addOption(Option.builder("p").longOpt("page-num")
				.hasArg().argName("start:end").desc("specify the page number").build());
	}
	
	@Override
	public void execute(String[] args) {
		// Retrieve the download path
				if (cmd.getArgList().size() < 1)
					printHelpThenExit("please give a download path");
				String downloadPath = cmd.getArgList().get(0);
				
				// Create a post downloader
				DcardPostDownloader downloader = new DcardPostDownloader(downloadPath);
				
				// Set gender
				if (cmd.hasOption("gender")) {
					String g = cmd.getOptionValue("gender");
					if (g.equals("M"))
						downloader.setTargetGender(Gender.MALE);
					else if (g.equals("F"))
						downloader.setTargetGender(Gender.FEMALE);
					else
						printHelpThenExit("wrong gender value");
				}
				
				// Set forum
				if (cmd.hasOption("forum")) {
					String forumName = cmd.getOptionValue("forum");
					DcardForum forum = null;
					try {
						forum = DcardForum.getByName(forumName);
					} catch (IllegalArgumentException e) {
						printHelpThenExit("forum: " + forumName + " doesn't exist");
					}
					downloader.setTargetForum(forum);
				}
				
				// Set if it downloads the post that don't have image
				if (cmd.hasOption("ex-no-img-posts")) {
					downloader.onlyWithImage(true);
				}
				
				// Set start and end page
				int startPage = DFT_START_PAGE;
				int endPage = DFT_END_PAGE;
				if (cmd.hasOption("page-num")) {
					String pageNumStr = cmd.getOptionValue("page-num");
					String[] pageNums = pageNumStr.split(":");
					try {
						startPage = Integer.parseInt(pageNums[0]);
						endPage = Integer.parseInt(pageNums[1]);
					} catch (NumberFormatException e) {
						printHelpThenExit("page number format error");
					}
				}
				
				// Download posts
				downloader.downloadPosts(startPage, endPage);
	}

	@Override
	public String getActionName() {
		return ACTION_NAME;
	}
	
}
