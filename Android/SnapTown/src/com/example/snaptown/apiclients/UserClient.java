package com.example.snaptown.apiclients;

import com.example.snaptown.helpers.ApiHelper;
import com.example.snaptown.models.UserModel;

public class UserClient {

	private final static String POST_USER_ROUTE = "auth";
	
	public static void postUserInfo(UserModel user){
		ApiHelper.post(POST_USER_ROUTE, user.toString());
	}
}
