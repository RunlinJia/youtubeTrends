package youtubetrends.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import youtubetrends.model.Likes;
import youtubetrends.model.SearchHistory;

public class SearchHistoryDao {
	protected ConnectionManager connectionManager;

	private static SearchHistoryDao instance = null;
	protected SearchHistoryDao() {
		connectionManager = new ConnectionManager();
	}
	public static SearchHistoryDao getInstance() {
		if(instance == null) {
			instance = new SearchHistoryDao();
		}
		return instance;
	}
	
	public SearchHistory create(SearchHistory searchHistory) throws SQLException {
		String insertSearchHistory =
			"INSERT INTO SearchHistory(SearchTime, SearchKeyWords, Counter, UserId)\r\n"
			+ "	VALUES(?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertSearchHistory,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setTimestamp(1, new Timestamp(searchHistory.getSearchTime().getTime()));
			insertStmt.setString(2, searchHistory.getSearchKeyWords());
			insertStmt.setInt(3, searchHistory.getCounter());
			insertStmt.setInt(4, searchHistory.getUser().getUserId());
			insertStmt.executeUpdate();

			resultKey = insertStmt.getGeneratedKeys();
			int ID = -1;
			if(resultKey.next()) {
				ID = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			searchHistory.setID(ID);
			return searchHistory;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(insertStmt != null) {
				insertStmt.close();
			}
			if(resultKey != null) {
				resultKey.close();
			}
		}
	}
	
	/**
	 * Get the SearchHistory record by fetching it from your MySQL instance.
	 * This runs a SELECT statement and returns a single SearchHistory instance.
	 */
	public SearchHistory getSearchHistoryBySearchKeyWords(String searchKeyWords) throws SQLException {
		String selectSearchHistory =
			"SELECT ID,SearchTime,SearchKeyWords,Counter,UserId\r\n"
			+ "FROM SearchHistory\r\n"
			+ "WHERE SearchKeyWords=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectSearchHistory);
			selectStmt.setString(1, searchKeyWords);
			results = selectStmt.executeQuery();
			//TODO:change name
			UsersDao usersDao = UsersDao.getInstance();
			
			if(results.next()) {
				int resultID = results.getInt("ID");
				Date searchTime = new Date(results.getTimestamp("SearchTime").getTime());
				String resultSearchKeyWords = results.getString("SearchKeyWords");
				int counter = results.getInt("Counter");
				int userId = results.getInt("UserId");
				//TODO:change name
				Users user = usersDao.getUsersById(userId);
				
				SearchHistory searchHistory = new SearchHistory(resultID, searchTime, resultSearchKeyWords, counter, user);
				return searchHistory;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
	}
	
	/**
	 * Get the all the SearchHistory for a User.
	 */
	public List<SearchHistory> getSearchHistoryByUserId(int userId) throws SQLException {
		List<SearchHistory> allSearchHistory = new ArrayList<SearchHistory>();
		String selectSearchHistory =
			"SELECT ID,SearchTime,SearchKeyWords,Counter,UserId\r\n"
			+ "FROM SearchHistory\r\n"
			+ "WHERE UserId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectSearchHistory);
			selectStmt.setInt(1, userId);
			results = selectStmt.executeQuery();
			//TODO:change name
			UsersDao usersDao = UsersDao.getInstance();
			while(results.next()) {
				int resultID = results.getInt("ID");
				Date searchTime = new Date(results.getTimestamp("SearchTime").getTime());
				String resultSearchKeyWords = results.getString("SearchKeyWords");
				int counter = results.getInt("Counter");
				int userId = results.getInt("UserId");
				//TODO:change name
				Users user = usersDao.getUsersById(userId);
				
				SearchHistory searchHistory = new SearchHistory(resultID, searchTime, resultSearchKeyWords, counter, user);

				allSearchHistory.add(searchHistory);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return allSearchHistory;
	}

	/**
	 * Update the counter of the SearchHistory instance.
	 * This runs a UPDATE statement.
	 */
	public SearchHistory updateCounter(SearchHistory searchHistory, int newCounter) throws SQLException {
		String updateSearchHistory = "UPDATE SearchHistory SET Counter=? WHERE ID=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateSearchHistory);

			updateStmt.setInt(1, newCounter);
			updateStmt.setInt(2, searchHistory.getID());
			updateStmt.executeUpdate();

			// Update the like @param before returning to the caller.
			searchHistory.setCounter(newCounter);
			return searchHistory;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}
	
	/**
	 * Delete the Likes instance.
	 * This runs a DELETE statement.
	 */
	public SearchHistory delete(SearchHistory searchHistory) throws SQLException {
		String deleteSearchHistory = "DELETE FROM SearchHistory WHERE ID=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteSearchHistory);
			deleteStmt.setInt(1, searchHistory.getID());
			deleteStmt.executeUpdate();

			// Return null so the caller can no longer operate on the Persons instance.
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}
}
