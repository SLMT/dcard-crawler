package slmt.crawler.dcard;

import java.io.File;
import java.io.IOException;

import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.util.IOUtils;

import com.alibaba.fastjson.JSON;

public class ArticleRetriever {
	
	private static File inDir, outDir;

	public static void main(String[] args) {

		// 檢查參數數目
		if (args.length < 2) {	
			System.out.println("Arguments: [Input Dir] [Output Dir]");
			return;
		}

		// 取出參數
		inDir = new File(args[0]);
		outDir = new File(args[1]);
		
		// 檢查輸出的資料夾是否存在
		if (!outDir.exists())
			outDir.mkdir();
		
		try {
			// Total Timer
			long totalStart = System.nanoTime();
			
			// 逐檔處理
			for (String fileName : inDir.list()) {
				// 取出檔案名稱
				String postIdStr = fileName.substring(0, 5);
				String fileTypeStr = fileName.substring(6);

				if (fileTypeStr.equals("json")) {
					// Timer
					long start = System.nanoTime();
					
					// 讀取 json 字串
					String json = IOUtils.loadAsString(new File(inDir, fileName));
					
					// 將文章轉成 java 物件
					Post post = JSON.parseObject(json, Post.class);
					
					// 將文章內容存檔
					IOUtils.saveToAFile(outDir, postIdStr + ".txt", post.version.get(0).content);

					long time = System.nanoTime() - start;
					System.out.println("Process post." + postIdStr + " took "
							+ time / 1000000 + "ms.");
				}
			}

			// Total Timer end
			long totalTime = System.nanoTime() - totalStart;
			System.out.println("Whole program took " + totalTime / 1000000
					+ "ms.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
