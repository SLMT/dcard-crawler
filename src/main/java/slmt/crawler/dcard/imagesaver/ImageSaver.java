package slmt.crawler.dcard.imagesaver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import slmt.crawler.dcard.analyzer.Parser;
import slmt.crawler.dcard.api.DcardForumAPI;
import slmt.crawler.dcard.api.DcardPostAPI;
import slmt.crawler.dcard.api.ImgurAPI;
import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpUtils;
import slmt.crawler.dcard.util.IOUtils;

public class ImageSaver {

	public static void main(String[] args) throws IOException {
		File outDir;
		
		// 檢查參數數目
		if (args.length < 1) {
			System.out.println("Arguments: [Output Dir]");
			return;
		}

		// 取出參數
		outDir = new File(args[0]);

		// 檢查輸出的資料夾是否存在
		if (!outDir.exists())
			outDir.mkdir();
		
		downloadImages(outDir);
	}

	private static void downloadImages(File outputDir) throws IOException {
		int numOfPosts = 100;
		int hasFind = 0;
		List<ImageDownloadInfo> images = new LinkedList<ImageDownloadInfo>();

		// 取得圖片的網址
		System.out.println("Retrieving image urls...");
		for (int page = 1; hasFind < numOfPosts; page++) {
			// 取得此頁的文章列表
			List<PostInfo> infos = DcardForumAPI.getPostList("sex", page);

			// 依序取得文章
			for (PostInfo info : infos) {
				if (!info.pinned && info.member.gender != null
						&& info.member.gender.equals("F")) {
					Post post = DcardPostAPI.downloadPost(info.id);

					List<String> urls = Parser.getImageURLs(post.version.get(0).content);
					int num = 0;
					for (String imageUrl : urls) {
						images.add(new ImageDownloadInfo(
								ImgurAPI.convertToDownloadURL(imageUrl), info.id + "_"
										+ num + ".jpg"));
						num++;
					}
				}
				hasFind++;
			}
		}
		System.out.println("All urls are retrieved.");

		// 下載檔案
		System.out.println("Start downloading images...");
		for (ImageDownloadInfo image : images) {
			InputStream in = HttpUtils.constructInputStream(image.url);
			IOUtils.saveToAFile(outputDir, image.fileName, in);
		}
		System.out.println("Downloading completed.");
	}
}
