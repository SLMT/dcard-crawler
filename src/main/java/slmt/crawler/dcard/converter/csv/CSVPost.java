package slmt.crawler.dcard.converter.csv;

import java.util.LinkedList;
import java.util.List;

public class CSVPost {

	// 文章基本資料
	public int pid; // 文章 id
	public String fourm; // 文章類別
	public boolean pinned; // 是否置頂
	public int reply; // 回覆的原文編號，如果這篇不是回文，這個值就是 -1
	public long createdAt; // 發文時間
	public long updatedAt; // 最後更新時間（有人留言也算更新）
	public int likeCount; // 喜歡的人數

	// 發文者資料
	public String posterGender;
	public String posterSchool;
	public String posterDepart;
	public boolean anonymousDepartment; // 是否隱藏系級
	public boolean anonymousSchool; // 是否隱藏學校

	// 文章內容
	public String title;
	public String content;
	
	// 留言 (不會被 toString)
	public List<CSVComment> comments = new LinkedList<CSVComment>();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(pid);
		sb.append(",");
		sb.append("\"" + fourm + "\"");
		sb.append(",");
		sb.append(pinned);
		sb.append(",");
		sb.append(reply);
		sb.append(",");
		sb.append(createdAt);
		sb.append(",");
		sb.append(updatedAt);
		sb.append(",");
		sb.append(likeCount);
		sb.append(",");
		sb.append("\"" + posterGender + "\"");
		sb.append(",");
		sb.append("\"" + posterSchool + "\"");
		sb.append(",");
		sb.append("\"" + posterDepart + "\"");
		sb.append(",");
		sb.append(anonymousDepartment);
		sb.append(",");
		sb.append(anonymousSchool);
		sb.append(",");
		sb.append("\"" + title + "\"");
		sb.append(",");
		sb.append("\"" + content + "\"");
		
		return sb.toString();
	}
}
