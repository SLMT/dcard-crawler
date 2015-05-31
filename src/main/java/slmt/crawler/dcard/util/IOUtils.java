package slmt.crawler.dcard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
	
	private static final int IO_BUFFER_SIZE = 4096;

	/**
	 * 將給定的 input stream 傳來的資料轉存到檔案中。
	 * 
	 * @param saveDir 存檔的資料夾
	 * @param saveFileName 存檔的檔名
	 * @param in 輸入的 input stream
	 */
	public static void saveToAFile(File saveDir, String saveFileName, InputStream in)
			throws IOException {
		FileOutputStream out = new FileOutputStream(new File(saveDir, saveFileName));

		byte[] buffer = new byte[IO_BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1)
			out.write(buffer, 0, bytesRead);

		in.close();
		out.close();
	}
}
