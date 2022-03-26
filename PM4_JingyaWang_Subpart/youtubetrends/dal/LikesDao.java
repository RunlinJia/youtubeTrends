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

public class LikesDao {
	protected ConnectionManager connectionManager;
	
	private static LikesDao  instance = null;
	protected LikesDao() {
		connectionManager = new ConnectionManager();
	}
	public static LikesDao  getInstance() {
		if(instance == null) {
			instance = new LikesDao();
		}
		return instance;
	}


	public Likes create(Likes like) throws SQLException {
		String insertLike =
			"INSERT INTO Likes(VideoId, UserId, Created)\r\n"
			+ "	VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertLike,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setInt(1, like.getVideo().getVideoId());
			insertStmt.setInt(2, like.getUser().getUserId());
			insertStmt.executeUpdate();

			resultKey = insertStmt.getGeneratedKeys();
			int likesId = -1;
			if(resultKey.next()) {
				likesId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			like.setLikesId(likesId);
			return like;
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
	 * Get the Likes record by fetching it from your MySQL instance.
	 * This runs a SELECT statement and returns a single Likes instance.
	 */
	public Likes getLikeById(int likesId) throws SQLException {
		String selectLike =
			"SELECT LikesId,VideoId,UserId,Creadted\r\n"
			+ "FROM Likes\r\n"
			+ "WHERE LikesId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectLike);
			selectStmt.setInt(1, likesId);
			results = selectStmt.executeQuery();
			//TODO:change name
			VideosDao videosDao = VideosDao.getInstance();
			UsersDao usersDao = UsersDao.getInstance();
			
			if(results.next()) {
				int videoId = results.getInt("VideoId");
				int userId = results.getInt("UserId");
				Date created = new Date(results.getTimestamp("Created").getTime());
				//TODO:change name
				Videos video = videosDao.getVideosById(videoId);
				Users user = usersDao.getUsersById(userId);
				
				Likes like = new Likes(likesId, video, user);
				return like;
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
	 * Update the created of the Likes instance.
	 * This runs a UPDATE statement.
	 */
	public Likes updateCreated(Likes like) throws SQLException {
		String updateLike = "UPDATE Likes SET Created=? WHERE LikesId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateLike);

			// Sets the Created timestamp to the current time.
			Date newCreatedTimestamp = new Date();
			updateStmt.setTimestamp(1, new Timestamp(newCreatedTimestamp.getTime()));
			updateStmt.setInt(2, like.getLikesId());
			updateStmt.executeUpdate();

			// Update the like @param before returning to the caller.
			like.setCreated(newCreatedTimestamp);
			return like;
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
	 * Get the all the Likes for a video.
	 */
	public List<Likes> getLikesByVideoId(int videoId) throws SQLException {
		List<Likes> likes = new ArrayList<Likes>();
		String selectLikes =
			"SELECT LikesId,VideoId,UserId,Created " +
			"FROM Likes " +
			"WHERE VideoId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectLikes);
			selectStmt.setInt(1, videoId);
			results = selectStmt.executeQuery();
			//TODO:change name
			VideosDao videosDao = VideosDao.getInstance();
			UsersDao usersDao = UsersDao.getInstance();
			while(results.next()) {
				int likesId = results.getInt("LikesId");
				int resultVideoId = results.getInt("VideoId");
				int userId = results.getInt("UserId");

				//TODO:change name
				Videos video = videosDao.getVideosById(resultVideoId);
				Users user = usersDao.getUsersById(userId);
				Date created =  new Date(results.getTimestamp("Created").getTime());
				
				Likes like = new Likes(likesId, video, user, created);
				likes.add(like);
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
		return likes;
	}
	
	/**
	 * Delete the Likes instance.
	 * This runs a DELETE statement.
	 */
	public Likes delete(Likes like) throws SQLException {
		String deleteLike = "DELETE FROM Likes WHERE LikesId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteLike);
			deleteStmt.setInt(1, like.getLikesId());
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
