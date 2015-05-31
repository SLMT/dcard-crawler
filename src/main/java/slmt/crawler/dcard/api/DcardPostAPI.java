package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DcardPostAPI {

	private static final String POST_API_PREFIX = "https://www.dcard.tw/api/post/all/";

	/**
	 * 建立一個下載指定 Dcard post 的 input stream.
	 * 
	 * @param postId 想要下載的 Post Id
	 * @return 如果文章存在的話，傳回一個 input stream；失敗或者不存在皆傳回 null
	 */
	public static InputStream constructInputStream(int postId) {
		String postUrl = POST_API_PREFIX + postId;

		try {
			// 註：下面這段 code 在 eclipse 中似乎會沒辦法正常執行
			// 不過包成 jar 檔似乎就沒問題了
			
			// Set request
			HttpsURLConnection con = (HttpsURLConnection) new URL(postUrl)
					.openConnection();
			con.setRequestMethod("GET");

			// Check response status
			int responseCode = con.getResponseCode();
			if (responseCode != 200)
				return null;

			return con.getInputStream();
		} catch (IOException e) {
			System.out.println("Exception when " + postUrl + ": "
					+ e.getMessage());
			return null;
		}
	}

}
