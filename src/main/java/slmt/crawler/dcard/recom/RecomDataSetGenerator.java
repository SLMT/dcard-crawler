package slmt.crawler.dcard.recom;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.util.IOUtils;

import com.alibaba.fastjson.JSON;

public class RecomDataSetGenerator {

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
			List<PostSnippet> snippets = new LinkedList<PostSnippet>();
			
			// 逐檔處理
			for (String fileName : inDir.list()) {
				// 取出檔案名稱
				String postIdStr = fileName.substring(0, 6);
				String fileTypeStr = fileName.substring(7);

				if (fileTypeStr.equals("json")) {
					// 讀取 json 字串
					String json = IOUtils.loadAsString(new File(inDir, fileName));
					
					// 將文章轉成 java 物件
					Post post = JSON.parseObject(json, Post.class);
					
					// 取得文章 id
					int postId = Integer.parseInt(postIdStr);
					
					// 建立 snippet
					snippets.add(new PostSnippet(postId, post));
					
					// 轉換成簡易文章並存擋
					SimplePost sp = new SimplePost(postId, post);
					IOUtils.saveToAFile(outDir, postIdStr + ".json", JSON.toJSONString(sp));
				}
			}
			
			// 輸出摘要
			IOUtils.saveToAFile(outDir, "list.json", JSON.toJSONString(snippets));

			// Total Timer end
			long totalTime = System.nanoTime() - totalStart;
			System.out.println("Whole program took " + totalTime / 1000000
					+ "ms.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
