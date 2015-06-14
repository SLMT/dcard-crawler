package slmt.crawler.dcard.imagesaver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import slmt.crawler.dcard.analyzer.Parser;
import slmt.crawler.dcard.api.DcardForumAPI;
import slmt.crawler.dcard.api.DcardPostAPI;
import slmt.crawler.dcard.api.ImgurAPI;
import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpUtils;
import slmt.crawler.dcard.util.IOUtils;

public class LongTermImageDaemon {

	private static final long SLEEPING_TIME = 60000; // in ms.

	private static File outputDir = new File("sex_images");

	public static void main(String[] args) throws IOException {
		// 檢查參數
		if (args.length < 1) {
			System.out.println("Arguments: [Output Dir]");
			return;
		}
		outputDir = new File(args[0]);
		if (!outputDir.exists())
			outputDir.mkdir();

		// Phase 1: 取得之前文章中的圖片
		System.out.println("=== Start phase 1: Retrieve all previous images ===");
		int lastChecked = scanPreviousPosts();
		System.out.println("=== Phase 1 finished. The posts before id: " + lastChecked
				+ " have been checked. ===");

		// Phase 2: 等待並取得新文章中的圖片
		System.out
				.println("=== Start phase 2: Retrieve the images in the new posts ===");
		retrieveNewImages(lastChecked);
	}

	private static int scanPreviousPosts() throws IOException {
		int page = 1, maxPostId = -1;
		List<PostInfo> infos = DcardForumAPI.getPostList("sex", page);
		while (infos.size() > 0) {
			// 依序取出文章
			for (PostInfo info : infos) {
				if (!info.pinned) {
					if (info.id > maxPostId)
						maxPostId = info.id;
					checkPost(info);
				}
			}

			// 取得下一篇文章
			page++;
			infos = DcardForumAPI.getPostList("sex", page);
		}

		return maxPostId;
	}

	private static void retrieveNewImages(int lastCheckedPostId) {
		// 無限迴圈
		while (true) {
			// 檢查是否有新文章
			int page = 1, maxPostId = -1;
			boolean reachStopPoint = false;
			List<PostInfo> infos = DcardForumAPI.getPostList("sex", page);
			while (infos.size() > 0) {
				// 依序取出文章
				for (PostInfo info : infos) {
					if (!info.pinned) {
						// 紀錄此次最大的 id
						if (info.id > maxPostId)
							maxPostId = info.id;

						// 檢查是否碰到上次最後檢查到的文章
						if (info.id <= lastCheckedPostId) {
							reachStopPoint = true;
							break;
						}

						checkPost(info);
					}
				}

				if (reachStopPoint)
					break;

				// 取得下一篇文章
				page++;
				infos = DcardForumAPI.getPostList("sex", page);
			}

			// 記錄這一輪看到的最新文章 id
			lastCheckedPostId = maxPostId;

			// 睡一下
			try {
				Thread.sleep(SLEEPING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void checkPost(PostInfo info) {
		try {
			if (info.member.gender != null && info.member.gender.equals("F")) {

				// 下載文章
				Post post = DcardPostAPI.downloadPost(info.id);

				// 取得文章中的圖片
				List<String> urls = Parser
						.getImageURLs(post.version.get(0).content);

				// 送出訊息
				if (urls.size() > 0)
					System.out
							.println("Start downloading the images in post no."
									+ info.id + "...");

				// 逐一檢視圖片網址
				int num = 0;
				for (String imageUrl : urls) {
					// 建立下載網址與儲存檔名
					String downloadUrl = ImgurAPI
							.convertToDownloadURL(imageUrl);
					String fileName = info.id + "_" + num + ".jpg";

					System.out.println("Downloading Image '" + fileName
							+ "'...");

					// 下載圖片
					InputStream in = HttpUtils
							.constructInputStream(downloadUrl);
					IOUtils.saveToAFile(outputDir, fileName, in);
					num++;

					System.out.println("Image '" + fileName
							+ "' has been downloaded.");
				}
			}
		} catch (Exception e) {
			System.out.println("Occur an error when checking post " + info.id);
			e.printStackTrace();
		}
	}
}
