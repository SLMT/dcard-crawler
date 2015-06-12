package slmt.crawler.dcard.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOUtils {

	private static final int IO_BUFFER_SIZE = 4096;

	/**
	 * 將給定的 input stream 傳來的資料轉存到檔案中。
	 * 
	 * @param saveDir
	 *            存檔的資料夾
	 * @param saveFileName
	 *            存檔的檔名
	 * @param in
	 *            輸入的 input stream
	 */
	public static void saveToAFile(File saveDir, String saveFileName,
			InputStream in) throws IOException {
		// 開啟輸出串流
		FileOutputStream out = new FileOutputStream(new File(saveDir,
				saveFileName));

		// 利用 buffer 轉存資料
		byte[] buffer = new byte[IO_BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1)
			out.write(buffer, 0, bytesRead);

		// 關閉串流
		in.close();
		out.close();
	}

	/**
	 * 將給定的字串儲存到檔案中。
	 * 
	 * @param saveDir
	 *            存檔的資料夾
	 * @param saveFileName
	 *            存檔的檔名
	 * @param str
	 *            想要儲存的字串
	 */
	public static void saveToAFile(File saveDir, String saveFileName, String str)
			throws IOException {
		// 開啟輸出串流
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(saveDir, saveFileName)), "UTF-8"));

		// 輸出
		out.append(str);

		// 關閉串流
		out.close();
	}

	/**
	 * 將指定檔案的內容讀出，並組合成一個字串。
	 * 
	 * @param file
	 *            檔案名稱
	 * @return 檔案內容組成的字串
	 */
	public static String loadAsString(File file) throws IOException {
		return loadAsString(new FileInputStream(file));
	}

	/**
	 * 將指定 input stream 讀出，並組合成一個字串。
	 * 
	 * @param inStream
	 *            要讀取的 input stream
	 * @return input stream 組成的字串
	 */
	public static String loadAsString(InputStream inStream) throws IOException {
		// 開啟輸入串流
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream,
				"UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();

		// 讀檔
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();

		// 轉成字串
		return response.toString();
	}
}
