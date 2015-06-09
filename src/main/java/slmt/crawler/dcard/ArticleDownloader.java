package slmt.crawler.dcard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import slmt.crawler.dcard.api.DcardPostAPI;
import slmt.crawler.dcard.util.IOUtils;
import slmt.crawler.dcard.util.TimeUtils;

public class ArticleDownloader {

	private static File dirLocation;
	private static int startIndex, endIndex;

	public static void main(String[] args) {

		// 檢查參數數目
		if (args.length < 3) {
			System.out.println("Arguments: [Save Dir] [Start Id] [End Id]");
			return;
		}

		// 取出參數
		try {
			dirLocation = new File(args[0]);
			startIndex = Integer.parseInt(args[1]);
			endIndex = Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
			System.out.println("Arguments must be integers.");
			return;
		}

		// 檢查儲存的資料夾是否存在
		if (!dirLocation.exists())
			dirLocation.mkdir();

		// 輸出開始時間
		System.out.println("Start time: " + TimeUtils.getCurrentDateString());

		// 下載文章
		try {
			// 抓取給定範圍內的文章
			for (int i = startIndex; i <= endIndex; i++) {
				long startTime = System.nanoTime();

				// 取得下載串流
				InputStream in = DcardPostAPI.constructInputStream(i);

				if (in == null)
					System.out.println("Post no." + i + " is not found.");
				else {
					// 儲存檔案
					IOUtils.saveToAFile(dirLocation, String.format("%06d", i)
							+ ".json", in);
					
					// 輸出下載時間
					long time = (System.nanoTime() - startTime) / 1000000;
					System.out.println("Post no." + i + " is downloaded in "
							+ time + " ms.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 輸出結束時間
		System.out.println("Stop time: " + TimeUtils.getCurrentDateString());
	}
}
