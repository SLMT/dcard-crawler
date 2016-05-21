package slmt.crawler.dcard;

import java.io.IOException;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;

public class Test {

	public static void main(String[] args) throws IOException {
		DcardPostDownloader downloader = new DcardPostDownloader("/tmp/posts");
		downloader.setTargetForum(DcardForum.BOOK);
		downloader.onlyWithImage(true);
		downloader.downloadPosts(20);
	}

}
