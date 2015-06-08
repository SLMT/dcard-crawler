package slmt.crawler.dcard.recom;

import slmt.crawler.dcard.json.Post;

public class PostSnippet {
	
	private static final int SNIPPET_LENGTH = 40;
	
	public int id;
	public String title;
	public String snippet;
	
	public PostSnippet() {
		
	}
	
	public PostSnippet(int id, Post post) {
		this.id = id;
		this.title = post.version.get(0).title;
		this.snippet = post.version.get(0).content;
		
		// 裁減文章
		if (snippet.length() > SNIPPET_LENGTH) {
			snippet = snippet.substring(0, SNIPPET_LENGTH);
			snippet += "...";
		}
	}
	
}
