package slmt.crawler.dcard;

import java.io.IOException;
import java.util.List;

import slmt.crawler.dcard.api.DcardForumAPI;
import slmt.crawler.dcard.json.PostInfo;

public class Test {

	public static void main(String[] args) throws IOException {
//		Action action = new TopAction();
//		action.execute(new String[]{"fetch-post", "-e", "-f", "PHOTOGRAPHY", "10", "/tmp/posts"});
//		action.execute(new String[]{"fetch-image", "/tmp/posts", "/tmp/posts"});
		
		List<PostInfo> list = DcardForumAPI.getPostList();
		System.out.println(list);
	}

}
