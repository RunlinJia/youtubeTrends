package youtubetrends.model;

import java.util.Date;

public class Likes {
	protected int likesId;
	protected Videos video;
	protected Users user;
	protected Date created;

	public Likes(int likesId, Videos video, Users user, Date created) {
		this.likesId = likesId;
		this.video = video;
		this.user = user;
		this.created = created;
	}

	public Likes(int likesId) {
		this.likesId = likesId;
	}

	public Likes(Videos video, Users user, Date created) {
		this.video = video;
		this.user = user;
		this.created = created;
	}

	public int getLikesId() {
		return likesId;
	}

	public void setLikesId(int likesId) {
		this.likesId = likesId;
	}

	public Videos getVideo() {
		return video;
	}

	public void setVideo(Videos video) {
		this.video = video;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
