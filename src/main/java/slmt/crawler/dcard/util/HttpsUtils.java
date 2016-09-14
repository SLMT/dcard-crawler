package slmt.crawler.dcard.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsUtils {
	
	/**
	 * 建立一個用於下載的 input stream 到指定的 url。
	 * 
	 * @param url input stream 的來源
	 * @return 如果該 url 連結成功，傳回一個 input stream；失敗或者不存在皆傳回 null
	 */
	public static InputStream constructInputStream(String url) {
		try {
			// 註：下面這段 code 在 eclipse 中似乎會沒辦法正常執行
			// 不過包成 jar 檔似乎就沒問題了
			
			// Set request
			HttpsURLConnection con = (HttpsURLConnection) new URL(url)
					.openConnection();
			
			// 如果沒有設定 User-Agent，就會被丟 403 Forbidden
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			con.setRequestMethod("GET");

			// Check response status
			int responseCode = con.getResponseCode();
			if (responseCode != 200)
				return null;

			return con.getInputStream();
		} catch (IOException e) {
			System.err.println("Exception when " + url + ": "
					+ e.getMessage());
			return null;
		}
	}
}
