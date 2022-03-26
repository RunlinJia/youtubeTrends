package youtubetrends.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import youtubetrends.model.BlogPosts;
import youtubetrends.model.BlogUsers;
import youtubetrends.model.Reshares;
import youtubetrends.model.Videos;

public class VideosDao {

  private static VideosDao instance = null;
  protected ConnectionManager connectionManager;

  protected VideosDao() {
    connectionManager = new ConnectionManager();
  }

  public static VideosDao getInstance() {
    if (instance == null) {
      instance = new VideosDao();
    }
    return instance;
  }

  public Videos create(Videos videos) throws SQLException {
    String insertReshare =
        "INSERT INTO Videos (TrendingDate,Title,PublishTime,Tags,Views,CommentCount,ThumbnailLink,Dislikes,CommentsDisabled,RatingsDisabled,VideoErrorOrRemoved,Description,CategoryId,CountryId,UserId)\n"
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;
    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertReshare, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setString(1, videos.getTrendingDate());
      insertStmt.setString(2, videos.getTitle());
      insertStmt.setTimestamp(3, videos.getPublishTime());
      insertStmt.setString(4, videos.getTags());
      insertStmt.setLong(5, videos.getViews());
      insertStmt.setLong(6, videos.getCommentCount());
      insertStmt.setString(7, videos.getThumbnailLink());
      insertStmt.setLong(8, videos.getDislikes());
      insertStmt.setBoolean(9, videos.isCommentsDisabled());
      insertStmt.setBoolean(10, videos.isRatingsDisabled());
      insertStmt.setBoolean(11, videos.isVideoErrorOrRemoved());
      insertStmt.setString(12, videos.getDescription());
      insertStmt.setString(13, videos.getCategory().getCategoryId());
      insertStmt.setString(14, videos.getCountry().getCountryId());
      insertStmt.setString(15, videos.getUser().getUserId());
      insertStmt.executeUpdate();

      // Retrieve the auto-generated key and set it, so it can be used by the caller.
      resultKey = insertStmt.getGeneratedKeys();
      int reshareId = -1;
      if (resultKey.next()) {
        reshareId = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      videos.setVideoId(reshareId);
      return videos;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
      if (resultKey != null) {
        resultKey.close();
      }
    }
  }

  public Videos delete(Videos videos) throws SQLException {
    String deleteReshare = "DELETE FROM Videos WHERE videoId=?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;
    try {
      connection = connectionManager.getConnection();
      deleteStmt = connection.prepareStatement(deleteReshare);
      deleteStmt.setInt(1, videos.getVideoId());
      deleteStmt.executeUpdate();
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (deleteStmt != null) {
        deleteStmt.close();
      }
    }
  }

  public Videos getVideosById(int videoId) throws SQLException {
    String selectReshare = "select VideoId, TrendingDate, Title, PublishTime, Tags, Views, CommentCount, ThumbnailLink, Dislikes, CommentsDisabled, RatingsDisabled, VideoErrorOrRemoved, Description, CategoryId, CountryId, UserId from videos where videoId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectReshare);
      selectStmt.setInt(1, videoId);
      results = selectStmt.executeQuery();
      CountriesDao countriesDao = CountriesDao.getInstance();
      CategoriesDao categoriesDao = CategoriesDao.getInstance();
      UsersDao usersDao = UsersDao.getInstance();
      if (results.next()) {
        int resultReshareId = results.getInt("VideoId");
        String trendingDate = results.getString("TrendingDate");
        String title = results.getString("Title");
        Timestamp publishTime = results.getTimestamp("PublishTime");
        String tags = results.getString("Tags");
        long views = results.getLong("Views");
        long commentCount = results.getLong("CommentCount");
        String thumbnailLink = results.getString("ThumbnailLink");
        long dislikes = results.getInt("Dislikes");
        boolean commentsDisabled = results.getBoolean("CommentsDisabled");
        boolean ratingsDisabled = results.getBoolean("RatingsDisabled");
        boolean videoErrorOrRemoved = results.getBoolean("VideoErrorOrRemoved");
        String description = results.getString("Description");
        Categories categories = categoriesDao.getCategoriesById(results.getInt("CategoryId"));
        Countries countries = countriesDao.getCountriesById(results.getInt("CountryId"));
        Users user = usersDao.getUsersById(results.getInt("UserId"));
        Videos reshare = new Videos(title, trendingDate, publishTime, tags, views, commentCount,
            thumbnailLink, dislikes, commentsDisabled, ratingsDisabled, videoErrorOrRemoved,
            description, categoryId, userId, countryId);
        return reshare;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return null;
  }

  public Videos updateTitle(Videos videos, String title)
      throws SQLException {
    String updateCreditCard = "UPDATE videos SET Title = ? WHERE VideoId = ?;";
    Connection connection = null;
    PreparedStatement updateStmt = null;
    try {
      connection = connectionManager.getConnection();
      updateStmt = connection.prepareStatement(updateCreditCard);
      updateStmt.setString(1,title);
      updateStmt.setInt(2, videos.getVideoId());
      updateStmt.executeUpdate();
      videos.setTitle(title);
      return videos;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (updateStmt != null) {
        updateStmt.close();
      }
    }
  }

}