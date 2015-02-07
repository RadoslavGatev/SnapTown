package com.example.snaptown.models;

import java.util.Date;

public class Media {
	public int mediaId;
	public String description;
	public Date uploadedOn;
	public String uploadedBy;
	public String townName;
	public int townId;

	public Media(int mediaId, String description, Date uploadedOn,
			String uploadedBy, String townName, int townId) {
		this.mediaId = mediaId;
		this.description = description;
		this.uploadedOn = uploadedOn;
		this.uploadedBy = uploadedBy;
		this.townName = townName;
		this.townId = townId;
	}
}
