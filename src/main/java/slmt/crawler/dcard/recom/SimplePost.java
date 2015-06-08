package slmt.crawler.dcard.recom;

import slmt.crawler.dcard.json.Post;

public class SimplePost {

	public int id;
	public String title;
	public String content;

	public SimplePost() {

	}

	public SimplePost(int id, Post post) {
		this.id = id;
		this.title = post.version.get(0).title;
		this.content = post.version.get(0).content;
	}
}
