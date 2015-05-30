package slmt.crawler.dcard.converter.json;

import java.util.LinkedList;
import java.util.List;

public class Comment {

	// 留言基本資料
	public String _id; // 意義不明，推測是資料庫 id
	public int like; // 喜歡數
	public boolean hidden; // 是否被刪除
	public int num; // 意義不明
	
	// 留言人相關
	public User member; // 留言人資料
	public boolean anonymous; // 留言人是否匿名
	public boolean host; // 是否是原 po
	
	// 與登入者相關
	public boolean currentUser; // 是否是登入的使用者
	public boolean isLiked; // 是否點喜歡
	
	// 留言內容
	public List<Version> version = new LinkedList<Version>();
}
