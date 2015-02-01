package com.example.snaptown.models;

import java.util.Date;

public class PostModel {

	public String postedBy;
	public Date postedOn;
	public Object data;
	public boolean isImage;
	
	public PostModel(String postedBy, Date postedOn, Object data, boolean isImage){
		this.postedBy = postedBy;
		this.postedOn = postedOn;
		this.data = data;
		this.isImage = isImage;
	}
}
