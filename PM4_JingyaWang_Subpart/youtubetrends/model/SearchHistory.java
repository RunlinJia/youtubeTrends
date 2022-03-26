package youtubetrends.model;

import java.util.Date;

public class SearchHistory {
	protected int ID;
	protected Date searchTime;
	protected String searchKeyWords;
	protected int counter;
	protected Users user;

	public SearchHistory(int iD, Date searchTime, String searchKeyWords, int counter, Users user) {
		ID = iD;
		this.searchTime = searchTime;
		this.searchKeyWords = searchKeyWords;
		this.counter = counter;
		this.user = user;
	}

	public SearchHistory(int iD) {
		ID = iD;
	}

	public SearchHistory(Date searchTime, String searchKeyWords, int counter, Users user) {
		this.searchTime = searchTime;
		this.searchKeyWords = searchKeyWords;
		this.counter = counter;
		this.user = user;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Date getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(Date searchTime) {
		this.searchTime = searchTime;
	}

	public String getSearchKeyWords() {
		return searchKeyWords;
	}

	public void setSearchKeyWords(String searchKeyWords) {
		this.searchKeyWords = searchKeyWords;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}
}
