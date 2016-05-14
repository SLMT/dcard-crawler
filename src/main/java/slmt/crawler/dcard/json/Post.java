package slmt.crawler.dcard.json;

import java.util.LinkedList;
import java.util.List;

public class Post {
	
	// 文章基本資料
	public long id;
	public String forum_alias; // 文章類別
	public boolean pinned; // 是否置頂
	public int reply; // 回覆的原文編號，如果這篇不是回文，這個值就是 -1
	public String createdAt; // 發文時間
	public String updatedAt; // 最後更新時間（有人留言也算更新）
	public int likeCount; // 喜歡的人數
	// public int like; // 同上，看不出差別何在
	
	// 發文者資料
	public User member; // 發文者資訊
	public boolean anonymousDepartment; // 是否隱藏系級
	public boolean anonymousSchool; // 是否隱藏學校
	
	// 與現在登入的使用者相關的資訊
	public boolean currentUser; // 是不是發文者
	public boolean isLiked; // 有沒有點喜歡
	public boolean follow; // 是否有追蹤
	
	// 文章內容
	public List<Version> version = new LinkedList<Version>();
	
	// 留言
	public List<Comment> comment = new LinkedList<Comment>();
}
