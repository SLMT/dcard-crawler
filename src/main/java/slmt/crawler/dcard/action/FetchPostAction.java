package slmt.crawler.dcard.action;

import org.apache.commons.cli.Option;

public class FetchPostAction extends Action {
	
	public static final String COMMAND_NAME = "fetch-post";
	
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
		
	}

	@Override
	public String getActionName() {
		return "fetch-post";
	}
	
}
