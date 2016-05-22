package slmt.crawler.dcard.json;

import java.util.LinkedList;
import java.util.List;

public class Post {
	
	// 文章基本資料
	public long id; // 編號
	public String title; // 標題
	public String content; // 內文
	public String excerpt; // 摘要
	public String createdAt; // 發文時間
	public String updatedAt; // 最後更新時間（有人留言也算更新）
	public String deletedAt; // 刪除時間 (真的會看到這個變數的內容嗎?)
	public int commentCount; // 回覆人數
	public int likeCount; // 喜歡的人數
	public List<String> tags = new LinkedList<String>();
	
	// 發文者資料
	public boolean anonymousSchool; // 是否隱藏學校
	public boolean anonymousDepartment; // 是否隱藏系所
	public String gender; // 性別
	public String school; // 學校
	public String department; // 系所
	public boolean currentMember; // 登入的人是不是發文者
	
	// 所屬版面資訊
	public boolean pinned; // 是否置頂
	public String forumId; // 版面編號? (不確定)
	public String forumName; // 版面名稱
	public String forumAlias; // 版面縮寫
	
	// 不確定用途
	public String replyId; // 也許是回覆的原文編號 (預設值是 null)
}
