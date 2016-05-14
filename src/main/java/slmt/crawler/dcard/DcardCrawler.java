package slmt.crawler.dcard;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;

public class DcardCrawler {

	public static void main(String[] args) {
		DcardPostDownloader downloader = new DcardPostDownloader("C:\\Users\\SLMT\\Desktop\\test\\kerker");
		
		downloader.onlyWithImage(true);
		downloader.setTargetForum(DcardForum.SEX);
		downloader.downloadPosts(1, 2);
	}
}
