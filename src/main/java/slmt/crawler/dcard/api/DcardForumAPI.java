package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;

import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpsUtils;
import slmt.crawler.dcard.util.IOUtils;


public class DcardForumAPI {
	
	public static String FOURM_SEX = "sex";
	
	private static String POST_LIST_BASE_URL = "https://www.dcard.tw/api/forum/";

	/**
	 * Gets the list of posts on a specified forum with given page number.
	 * 
	 * @param forum the forum containing posts
	 * @param pageNum the page number of the forum
	 * @return the list of post information
	 */
	public static List<PostInfo> getPostList(DcardForum forum, int pageNum) {
		try {
			String url = POST_LIST_BASE_URL + forum.getAlias() + "/" + pageNum;
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
