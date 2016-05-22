package slmt.crawler.dcard;

import java.io.IOException;

import slmt.crawler.dcard.action.Action;
import slmt.crawler.dcard.action.TopAction;

public class Test {

	public static void main(String[] args) throws IOException {
		Action action = new TopAction();
		action.execute(new String[]{"fetch-post", "-e", "-f", "PHOTOGRAPHY", "10", "/tmp/posts"});
		action.execute(new String[]{"fetch-image", "/tmp/posts", "/tmp/posts"});
	}

}
