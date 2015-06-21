package slmt.crawler.dcard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.util.IOUtils;

import com.alibaba.fastjson.JSON;

public class ArticleRetriever {
	
	private static File inDir, outDir;
	private static String outputFile;

	public static void main(String[] args) {

		// 檢查參數數目
		if (args.length < 3) {	
			System.out.println("Arguments: [Input Dir] [Output Dir] [Output File Name]");
			return;
		}

		// 取出參數
		inDir = new File(args[0]);
		outDir = new File(args[1]);
		outputFile = args[2];
		
		// 檢查輸出的資料夾是否存在
		if (!outDir.exists())
			outDir.mkdir();
		
		try {
			// Total Timer
			long totalStart = System.nanoTime();
			int maxLen = 0;
			String postIdOfLongestLine = "";
			
			// 開啟輸出檔案
			BufferedWriter out = IOUtils.getBufferedWriter(outDir, outputFile);
			
			// 逐檔處理
			for (String fileName : inDir.list()) {
				// 取出檔案名稱
				String postIdStr = fileName.substring(0, 6);
				String fileTypeStr = fileName.substring(7);

				if (fileTypeStr.equals("json")) {
					// Timer
					long start = System.nanoTime();
					
					// 讀取 json 字串
					String json = IOUtils.loadAsString(new File(inDir, fileName));
					
					// 將文章轉成 java 物件
					Post post = JSON.parseObject(json, Post.class);
					
					// 將文章內容存檔
					
					// => 先存標題
					StringBuilder sb = new StringBuilder();
					StringTokenizer st = new StringTokenizer(post.version.get(0).title);
					while (st.hasMoreTokens())
						sb.append(st.nextToken() + " ");
					// 重複十遍，讓 title 在處理時的權重提高
					for (int i = 0; i < 10; i++)
						write(out, postIdStr, sb.toString());
					
					// => 再存文章
					st = new StringTokenizer(post.version.get(0).content, "\r\n");
					while (st.hasMoreTokens()) {
						String line = st.nextToken();

						if (line.length() > maxLen) {
							maxLen = line.length();
							postIdOfLongestLine = postIdStr;
						}
						
						if (line.length() < 1000)
							write(out, postIdStr, line);
					}

					long time = System.nanoTime() - start;
					System.out.println("Process post." + postIdStr + " took "
							+ time / 1000000 + "ms.");
				}
			}

			// 關閉檔案
			out.close();

			// Total Timer end
			long totalTime = System.nanoTime() - totalStart;
			System.out.println("Whole program took " + totalTime / 1000000
					+ "ms.");
			System.out.println("The max. length of posts is " + maxLen
					+ " words in the post. " + postIdOfLongestLine);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void write(BufferedWriter out, String id, String content) throws IOException {
		out.append(id);
		out.append(" ");
		out.append(content);
		out.newLine();
	}
}
