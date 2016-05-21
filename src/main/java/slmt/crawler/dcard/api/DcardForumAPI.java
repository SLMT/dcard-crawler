package slmt.crawler.dcard.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;

import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.HttpsUtils;
import slmt.crawler.dcard.util.IOUtils;


public class DcardForumAPI {
	
	private static String DCARD_API_BASE_URL = "https://dcard.tw/_api";
	private static String FORUM_PART_URL = "/forums/";
	private static String URL_ARGUMENTS_PART1 = "/posts?popular=false";
	private static String URL_ARGUMENTS_PART2 = "&before=";
	
	/**
	 * 取回討論區首頁的前 30 篇（依發佈時間排序）文章列表。
	 * 
	 * @return 前 30 篇文章列表
	 */
	public static List<PostInfo> getPostList() {
		// https://dcard.tw/_api/posts?popular=false
		return getPostList(DCARD_API_BASE_URL + URL_ARGUMENTS_PART1);
	}
	
	/**
	 * 取回指定版面的前 30 篇（依發佈時間排序）文章列表。
	 * 
	 * @param forum 指定版面
	 * @return 前 30 篇文章列表
	 */
	public static List<PostInfo> getPostList(DcardForum forum) {
		// https://dcard.tw/_api/forums/[forum]/posts?popular=false
		String url = DCARD_API_BASE_URL + FORUM_PART_URL
				+ forum.getAlias() + URL_ARGUMENTS_PART1;
		return getPostList(url);
	}
	
	/**
	 * 取回指定版面，並出現在指定文章編號之前的前 30 篇（依發佈時間排序）文章列表。
	 * 
	 * @param forum 指定版面
	 * @param beforePostId 指定最晚文章編號（不包含該文章）
	 * @return 符合需求的前 30 篇文章列表
	 */
	public static List<PostInfo> getPostList(DcardForum forum, int beforePostId) {
		// https://dcard.tw/_api/forums/[forum]/posts?popular=false&before=[id]
		String url = DCARD_API_BASE_URL + FORUM_PART_URL
				+ forum.getAlias() + URL_ARGUMENTS_PART1
				+ URL_ARGUMENTS_PART2 + beforePostId;
		return getPostList(url);
	}
	
	private static List<PostInfo> getPostList(String url) {
		try {
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
