package slmt.crawler.dcard.converter.csv;

public class CSVComment {

	// 留言基本資料
	public int cid; // Comment id
	public int postId; // 屬於哪一個 Post
	public int like; // 喜歡數
	public boolean hidden; // 是否被刪除

	// 留言人相關
	public String posterGender;
	public String posterSchool;
	public String posterDepart;
	public boolean anonymous; // 留言人是否匿名
	public boolean host; // 是否是原 po

	// 留言內容
	public String content;
	public long postTime;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(cid);
		sb.append(",");
		sb.append(postId);
		sb.append(",");
		sb.append(like);
		sb.append(",");
		sb.append(hidden);
		sb.append(",");
		sb.append("\"" + posterGender + "\"");
		sb.append(",");
		sb.append("\"" + posterSchool + "\"");
		sb.append(",");
		sb.append("\"" + posterDepart + "\"");
		sb.append(",");
		sb.append(anonymous);
		sb.append(",");
		sb.append(host);
		sb.append(",");
		sb.append("\"" + content + "\"");
		sb.append(",");
		sb.append(postTime);
		
		return sb.toString();
	}
}
