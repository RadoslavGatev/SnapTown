package com.example.snaptown.models;

import java.util.Date;

public class Media {
	public int mediaId;
	public String description;
	public Date uploadedOn;
	public String uploadedBy;

	public Media(int mediaId, String description, Date uploadedOn,
			String uploadedBy) {
		this.mediaId = mediaId;
		this.description = description;
		this.uploadedOn = uploadedOn;
		this.uploadedBy = uploadedBy;
	}
}
