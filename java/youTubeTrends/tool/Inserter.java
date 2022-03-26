package youTubeTrends.tool;

import youTubeTrends.dal.VideoTrendsDao;
import youTubeTrends.model.VideoTrends;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Inserter {

	public static void main(String[] args) throws SQLException {
		// DAO instances.
		VideoTrendsDao videoTrendsDao = VideoTrendsDao.getInstance();

		// INSERT objects from our model.
		VideosDao videoDao = VideoDao.getInstance();
		VideoTrends videoTrends = new VideoTrends(videoDao.getVideoByVideoId(2886), new SimpleDateFormat("yyyy-MM-dd").parse("2022-02-01"),
				"synthesize holistic functionalities");
		videoTrends = videoTrendsDao.create(videoTrends);

		// READ.
		VideoTrends vt1 = videoTrendsDao.getVideoTrendsById(1);
		System.out.format("Reading VideoTrends: videoTitle:%s des:%s \n", vt1.getVideoId().getTitle(), vt1.getDescription());
		
		// delete
		videoTrendsDao.delete(videoTrendsDao.getVideoTrendsById(1));
	}
}
