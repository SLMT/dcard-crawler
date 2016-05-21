package slmt.crawler.dcard.api;

/**
 * Dcard 的子論壇名稱。目前主要是用來當作參數傳遞，以及提取別名 (alias)。
 * 注意 CCC = 3C，因為變數名稱不能是數字開頭 。
 * 
 * @author SLMT
 *
 */
public enum DcardForum {
	BOOK, ACG, GAME, CCC, FUNNY, BG, TREADING, TALK, GIRL, BOY,
	MOOD, MUSIC, TRAVEL, PHOTOGRAPHY, MOVIE, HOROSCOPES, LITERATURE, SPORT,
	PET, FOOD, HANDICRAFTS, JOB, STUDYABROAD, MARVEL, FRESHMAN, COURSE,
	EXAM, SEX, DCARD, WHYSOSERIOUS;
	
	public static DcardForum getByName(String name) {
		name = name.toUpperCase();
		return DcardForum.valueOf(name);
	}
	
	public String getAlias() {
		return this.name().toLowerCase();
	}
}
