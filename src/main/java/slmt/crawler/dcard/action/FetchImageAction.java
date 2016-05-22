package slmt.crawler.dcard.action;

import slmt.crawler.dcard.downloader.ImageDownloader;

public class FetchImageAction extends Action {
	
	public static final String ACTION_NAME = "fetch-image";

	@Override
	public void execute(String[] args) {
		// Download images
				if (cmd.hasOption("download-images")) {
					ImageDownloader imgDownloader = new ImageDownloader(downloadPath, downloadPath);
					imgDownloader.downloadImages();
				}
	}

	@Override
	public String getActionName() {
		return ACTION_NAME;
	}

}
