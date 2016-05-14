package slmt.crawler.dcard;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.downloader.DcardPostDownloader;
import slmt.crawler.dcard.downloader.DcardPostDownloader.Gender;
import slmt.crawler.dcard.downloader.ImageDownloader;

public class DcardCrawler {
	
	private static final String TEST_DIR = "C:\\Users\\SLMT\\Desktop\\test\\kerker";

	public static void main(String[] args) {
		downloadPosts();
		downloadImages();
	}
	
	private static void downloadPosts() {
		DcardPostDownloader downloader = new DcardPostDownloader(TEST_DIR);
		
		downloader.setTargetGender(Gender.FEMALE);
		downloader.onlyWithImage(true);
		downloader.setTargetForum(DcardForum.ALL);
		downloader.downloadPosts(1, 3);
	}
	
	private static void downloadImages() {
		ImageDownloader downloader = new ImageDownloader(TEST_DIR, TEST_DIR);
		downloader.downloadImages();
	}
}
