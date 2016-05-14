package slmt.crawler.dcard;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;
import slmt.crawler.dcard.downloader.DcardPostDownloader.Gender;

public class DcardCrawler {

	public static void main(String[] args) {
		DcardPostDownloader downloader = new DcardPostDownloader("C:\\Users\\SLMT\\Desktop\\test\\kerker");
		
		downloader.setTargetGender(Gender.FEMALE);
		downloader.onlyWithImage(true);
		downloader.setTargetForum(DcardForum.SEX);
		downloader.downloadPosts(1, 2);
	}
}
