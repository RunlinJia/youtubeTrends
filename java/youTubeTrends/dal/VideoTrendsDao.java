package youTubeTrends.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import youTubeTrends.model.VideoTrends;

public class VideoTrendsDao {

	private ConnectionManager connectionManager;
	private static VideoTrendsDao instance = null;

	public static VideoTrendsDao getInstance() {
		if (instance == null) {
			instance = new VideoTrendsDao();
		}
		return instance;
	}

	public VideoTrendsDao() {
		super();
		this.connectionManager = new ConnectionManager();
	}

	public VideoTrends create(VideoTrends videoTrends) throws SQLException {
		String insertVideo = "INSERT INTO VideoTrends(videoId, trendingDate, description) VALUES(?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertVideo);
			insertStmt.setInt(1, videoTrends.getVideoId());
			insertStmt.setTimestamp(2, new Timestamp(videoTrends.getTrendingDate().getTime()));
			insertStmt.setString(3, videoTrends.getDescription());
			insertStmt.executeUpdate();
			
			return videoTrends;
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
		}
	}

	public VideoTrends getVideoTrendsById(int videoTrendsId) throws SQLException {
		String selectVideoTrend = "SELECT videoTrendsId, videoId, trendingDate, description FROM VideoTrends WHERE videoTrendsId=?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectVideoTrend);
			selectStmt.setInt(1, videoTrendsId);
			results = selectStmt.executeQuery();
			if(results.next()) {
				Integer resultVideoTrendsId = results.getInt("videoTrendsId");
				Integer videoId = results.getInt("videoId");
				Date trendingDate = results.getDate("trendingDate");
				String description = results.getString("description");
				VideoTrends videoTrend = new VideoTrends(resultVideoTrendsId, videoId, trendingDate, description);
				return videoTrend;
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

	public VideoTrends update(VideoTrends videoTrend, String newDesc) throws SQLException {
		String updateVideoTrend = "UPDATE VideoTrends SET LastName=? WHERE videoTrendsId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateVideoTrend);
			updateStmt.setString(1, newDesc);
			updateStmt.setInt(2, videoTrend.getVideoTrendId());
			updateStmt.executeUpdate();
			
			videoTrend.setDescription(newDesc);
			return videoTrend;
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

	public VideoTrends delete(VideoTrends videoTrend) throws SQLException {
		String deleteVideoTrend = "DELETE FROM VideoTrends WHERE videoTrendsId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteVideoTrend);
			deleteStmt.setInt(1, videoTrend.getVideoTrendId());
			deleteStmt.executeUpdate();

			// Return null so the caller can no longer operate on the VideoTrends instance.
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
