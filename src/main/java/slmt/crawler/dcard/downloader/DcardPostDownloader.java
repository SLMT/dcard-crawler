package slmt.crawler.dcard.downloader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.api.DcardForumAPI;
import slmt.crawler.dcard.api.DcardPostAPI;
import slmt.crawler.dcard.json.Post;
import slmt.crawler.dcard.json.PostInfo;
import slmt.crawler.dcard.util.IOUtils;

public class DcardPostDownloader {
	Logger logger = Logger.getLogger(DcardPostDownloader.class.getName());
	
	private File saveDir;
	
	// Options
	private boolean onlyPostWithImage = false; // Not including comments
	private DcardForum forum = DcardForum.ALL;
	
	public DcardPostDownloader(String savePath) {
		saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
			
			if (logger.isLoggable(Level.INFO))
				logger.info("creates a new directory at " + saveDir);
		}
	}
	
	public void onlyWithImage(boolean enable) {
		onlyPostWithImage = enable;
	}
	
	public void setTargetForum(DcardForum forum) {
		this.forum = forum;
	}
	
	public void downloadPosts(int startPageNum, int endPageNum) {
		
		// For each page
		try {
			for (int page = startPageNum; page <= endPageNum; page++) {
				List<PostInfo> infos = DcardForumAPI.getPostList(forum, page);
				
				if (logger.isLoggable(Level.INFO))
					logger.info("retrieving page no." + page);
				
				// For each post in each page
				for (PostInfo info : infos) {
					// Retrieve the post
					Post post = DcardPostAPI.downloadPost(info.id);
					
					// Only saving the post with images (if set)
					if (onlyPostWithImage && !containImage(post))
						continue;
					
					// Save the post
					String fileName = String.format("%09d.txt", info.id);
					String basicPost = constructBasicPost(post);
					IOUtils.saveToAFile(saveDir, fileName, basicPost);
				}
				
				if (logger.isLoggable(Level.INFO))
					logger.info("page no." + page + " is downloaded");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean containImage(Post post) {
		String article = post.version.get(0).content;
		return article.contains("imgur.com");
	}
	
	private String constructBasicPost(Post post) {
		String title = post.version.get(0).title;
		String article = post.version.get(0).content;
		String oriUrl = "https://www.dcard.tw/f/" + forum.getAlias() + "/p/" + post.id;
		
		StringBuilder out = new StringBuilder();
		
		out.append("標題：").append(title).append('\n');
		
		out.append("文章發表時間：").append(post.createdAt).append('\n');
		
		out.append("發文者所屬：").append(post.member.school).append(' ');
		if (!post.anonymousDepartment)
			out.append(post.member.department).append(' ');
		if (post.member.gender.equals("M"))
			out.append("男");
		else
			out.append("女");
		out.append('\n');
		
		out.append("原文網址：").append(oriUrl).append('\n');
		
		out.append("文章內容：\n").append(article);
		
		return out.toString();
	}
}
