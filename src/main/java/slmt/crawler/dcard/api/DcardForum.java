package slmt.crawler.dcard.api;

public enum DcardForum {
	ALL, SEX;
	
	public String getAlias() {
		switch (this) {
		case ALL:
			return "all";
		case SEX:
			return "sex";
		default:
			return null;
		}
	}
}
