package slmt.crawler.dcard.action;

import org.apache.commons.cli.CommandLine;

import slmt.crawler.dcard.downloader.ImageDownloader;

public class FetchImageAction extends Action {
	
	public static final String ACTION_NAME = "fetch-image";

	@Override
	public void execute(String[] args) {
		CommandLine cmdLine = parse(args);
		
		// Retrieve the download path
		if (cmdLine.getArgs().length < 3)
			printHelpThenExit("please give more arguments");
		String postDirPath = cmdLine.getArgs()[1];
		String savePath = cmdLine.getArgs()[2];
		
		// Download images
		ImageDownloader imgDownloader = new ImageDownloader(postDirPath, savePath);
		imgDownloader.downloadImages();
	}

	@Override
	public String getCommandSyntax() {
		return COMMAND_PREFIX + " " + ACTION_NAME + " [options] [posts dir path] [save path]";
	}

}
