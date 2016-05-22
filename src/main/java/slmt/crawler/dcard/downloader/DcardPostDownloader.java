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
	
	public static enum Gender { ALL, MALE, FEMALE };
	
	private static final int REPORT_COUNT = 5;
	
	private File saveDir;
	
	// Options
	private boolean onlyPostWithImage = false; // Not including the images in comments
	private boolean redownloadExistings = false;
	private boolean dontCountRedownPosts = false;
	private Gender targetGender = Gender.ALL; // Kerker
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
		if (logger.isLoggable(Level.INFO))
			logger.info("only download the posts with images: " + enable);
		onlyPostWithImage = enable;
	}
	
	public void redownloadExistings(boolean enable) {
		if (logger.isLoggable(Level.INFO))
			logger.info("re-download existing posts: " + enable);
		redownloadExistings = enable;
	}
	
	public void dontCountRedownPosts(boolean enable) {
		if (logger.isLoggable(Level.INFO))
			logger.info("do not count re-downloaded posts: " + enable);
		dontCountRedownPosts = enable;
	}
	
	public void setTargetGender(Gender gender) {
		if (logger.isLoggable(Level.INFO))
			logger.info("only download the posts whose author is: " + gender.name());
		targetGender = gender;
	}
	
	public void setTargetForum(DcardForum forum) {
		if (logger.isLoggable(Level.INFO))
			logger.info("only download the posts in forum: " + forum.name());
		this.forum = forum;
	}
	
	public void downloadPosts(int numOfPosts) {
		downloadPosts(numOfPosts, -1);
	}
	
	public void downloadPosts(int numOfPosts, int lastPostId) {
		try {
			List<PostInfo> postList;
			int totalLoadCount = 0, newLoadCount = 0;
			while ((dontCountRedownPosts && newLoadCount < numOfPosts) ||
					(!dontCountRedownPosts && totalLoadCount < numOfPosts)) {
				// Retrieve a post list
				if (lastPostId < 0) {
					// Retrieve the first post list
					if (logger.isLoggable(Level.INFO))
						logger.info("retrieving the list of first 30 posts of " + forum + " forum");
					postList = DcardForumAPI.getPostList(forum);
				} else {
					// Retrieve posts before a given id
					if (logger.isLoggable(Level.INFO))
						logger.info("retrieving the list of posts before no." + lastPostId + " in " + forum + " forum");
					postList = DcardForumAPI.getPostList(forum, lastPostId);
				}
				
				// For each post in the list
				for (PostInfo info : postList) {
					lastPostId = info.id;
					
					// Only target gender
					if (targetGender != Gender.ALL) {
						if (targetGender == Gender.MALE &&
								!info.gender.equals("M"))
							continue;
						else if (targetGender == Gender.FEMALE &&
								info.gender.equals("M"))
							continue;
					}
					
					// Check if we have already had this post
					String fileName = String.format("%09d.txt", info.id);
					boolean existing = false;
					if (new File(saveDir, fileName).exists())
						existing = true;
					if (!redownloadExistings && existing)
						continue;
					
					// Retrieve the post
					Post post = DcardPostAPI.downloadPost(info.id);
					
					// Only saving the post with images (if set)
					if (onlyPostWithImage && !containImage(post))
						continue;
					
					// Save the post
					String basicPost = constructBasicPost(post);
					IOUtils.saveToAFile(saveDir, fileName, basicPost);
					
					// Wait for a second
					waitForASecond();
					
					// Check if we need more
					if (!existing)
						newLoadCount++;
					totalLoadCount++;
					if (totalLoadCount % REPORT_COUNT == 0 && logger.isLoggable(Level.INFO))
						logger.info(totalLoadCount + " posts have been downloaded. ("
								+ (totalLoadCount - newLoadCount) + " posts are re-downloaded.)");
					if ((dontCountRedownPosts && newLoadCount >= numOfPosts) ||
							(!dontCountRedownPosts && totalLoadCount >= numOfPosts))
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean containImage(Post post) {
		String article = post.content;
		return article.contains("imgur.com");
	}
	
	private String constructBasicPost(Post post) {
		String title = post.title;
		String article = post.content;
		String oriUrl = "https://www.dcard.tw/f/" + forum.getAlias() + "/p/" + post.id;
		
		StringBuilder out = new StringBuilder();
		
		out.append("標題：").append(title).append('\n');
		
		out.append("文章發表時間：").append(post.createdAt).append('\n');
		
		out.append("發文者所屬：").append(post.school).append(' ');
		if (!post.anonymousDepartment)
			out.append(post).append(' ');
		if (post.gender.equals("M"))
			out.append("男");
		else
			out.append("女");
		out.append('\n');
		
		out.append("原文網址：").append(oriUrl).append('\n');
		
		out.append("文章內容：\n").append(article);
		
		return out.toString();
	}
	
	private void waitForASecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
