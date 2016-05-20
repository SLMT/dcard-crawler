package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;

import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpsUtils;
import slmt.crawler.dcard.util.IOUtils;


public class DcardForumAPI {
	
	private static String POST_LIST_BASE_URL = "https://dcard.tw/_api/forums/";
	private static String URL_ARGUMENTS = "/posts?popular=false&before=";

	/**
	 * Gets the list of posts on a specified forum with given page number.
	 * 
	 * @param forum the forum containing posts
	 * @param beforePostId the latest post id (excluded)
	 * @return the list of post information
	 */
	public static List<PostInfo> getPostList(DcardForum forum, int beforePostId) {
		try {
			String url = POST_LIST_BASE_URL + forum.getAlias() + URL_ARGUMENTS
					+ beforePostId;
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
