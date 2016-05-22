package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;

import com.alibaba.fastjson.JSON;

import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.util.HttpsUtils;
import slmt.crawler.dcard.util.IOUtils;

public class DcardPostAPI {

	private static final String POST_API_PREFIX = "https://dcard.tw/_api/posts/";

	/**
	 * 建立一個下載指定 Dcard post 的 input stream.
	 * 
	 * @param postId
	 *            想要下載的 Post Id
	 * @return 如果文章存在的話，傳回一個 input stream；失敗或者不存在皆傳回 null
	 */
	public static InputStream constructInputStream(int postId) {
		return HttpsUtils.constructInputStream(POST_API_PREFIX + postId);
	}

	/**
	 * 下載指定的文章。
	 * 
	 * @param postId 指定文章的編號
	 * @return 如果文章存在，傳回指定文章的物件；不存在則回傳 null
	 */
	public static Post downloadPost(int postId) {
		try {
			InputStream in = HttpsUtils.constructInputStream(POST_API_PREFIX
					+ postId);
			String data = IOUtils.loadAsString(in);
			return JSON.parseObject(data, Post.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
