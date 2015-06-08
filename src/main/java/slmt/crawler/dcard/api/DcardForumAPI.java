package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;

import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpsUtils;
import slmt.crawler.dcard.util.IOUtils;


public class DcardForumAPI {
	
	private static String POST_LIST_BASE_URL = "https://www.dcard.tw/api/forum/";

	/**
	 * TODO Add contents
	 * 
	 * @param fourmAlias
	 * @param pageNum
	 * @return
	 */
	public static List<PostInfo> getPostList(String fourmAlias, int pageNum) {
		try {
			String url = POST_LIST_BASE_URL + fourmAlias + "/" + pageNum;
			InputStream in = HttpsUtils.constructInputStream(url);
			
			if (in == null)
				return null;
			
			String listString = IOUtils.loadAsString(in);
			
			return JSON.parseArray(listString, PostInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
