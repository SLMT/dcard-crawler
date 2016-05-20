package slmt.crawler.dcard;

import java.io.IOException;

import slmt.crawler.dcard.api.DcardPostAPI;
import slmt.crawler.dcard.json.Post;

public class Test {

	public static void main(String[] args) throws IOException {
		Post post = DcardPostAPI.downloadPost(224047755);
		System.out.println(post.content);
	}

}
