package youtubetrends.tools;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import youtubetrends.dal.LikesDao;
import youtubetrends.dal.SearchHistoryDao;
import youtubetrends.model.Likes;
import youtubetrends.model.SearchHistory;

public class Inserter {
	public static void main(String[] args) throws SQLException, ParseException {
		// DAO instances.
		LikesDao likesDao = LikesDao.getInstance();
		SearchHistoryDao searchHistoryDao = SearchHistoryDao.getInstance();
		
		// READ.
		Likes like1 = likesDao.getLikeById(8);
		System.out.format("Reading like: LikesId:%d VideoId:%d UserId:%d Created:%s \n",
				like1.getLikesId(), like1.getVideo().getVideoId(), 
				like1.getUser().getUserId(), like1.getCreated().toString());
		
		List<Likes> likeList = likesDao.getLikesByVideoId(44);
		for(Likes tempLike : likeList) {
			System.out.format("Reading like: LikesId:%d VideoId:%d UserId:%d Created:%s \n",
					tempLike.getLikesId(), tempLike.getVideo().getVideoId(), 
					tempLike.getUser().getUserId(), tempLike.getCreated().toString());
		}
		
		SearchHistory searchHistory1 = searchHistoryDao.getSearchHistoryBySearchKeyWords("Cats");
		System.out.format("Reading searchHistory: ID:%d SearchTime:%s SearchKeyWords:%s Counter:%s UserId:%d \n",
				searchHistory1.getID(), searchHistory1.getSearchTime().toString(),
				searchHistory1.getSearchKeyWords(), searchHistory1.getCounter(),
				searchHistory1.getUser().getUserId());
		
		List<SearchHistory> searchHistoryList = searchHistoryDao.getSearchHistoryByUserId(36);
		for(SearchHistory tempSH: searchHistoryList) {
			System.out.format("Reading searchHistory: ID:%d SearchTime:%s SearchKeyWords:%s Counter:%s UserId:%d \n",
					tempSH.getID(), tempSH.getSearchTime().toString(),
					tempSH.getSearchKeyWords(), tempSH.getCounter(),
					tempSH.getUser().getUserId());
		}
		
		// Update.
		likesDao.updateCreated(like1);
		
		searchHistoryDao.updateCounter(searchHistory1, 60);
		
		// Delete.
		
		likesDao.delete(like1);
		
		searchHistoryDao.delete(searchHistory1);
	}

}
