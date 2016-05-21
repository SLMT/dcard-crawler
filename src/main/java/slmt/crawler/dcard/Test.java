package slmt.crawler.dcard;

import java.io.IOException;
import java.util.List;

import slmt.crawler.dcard.api.DcardForum;
import slmt.crawler.dcard.api.DcardForumAPI;
import slmt.crawler.dcard.json.PostInfo;

public class Test {

	public static void main(String[] args) throws IOException {
		List<PostInfo> posts = DcardForumAPI.getPostList();
		System.out.println(posts.get(0).title);
		
		posts = DcardForumAPI.getPostList(DcardForum.BOOK);
		System.out.println(posts.get(0).title);
		
		posts = DcardForumAPI.getPostList(DcardForum.BOOK, 224062050);
		System.out.println(posts.get(0).title);
	}

}
