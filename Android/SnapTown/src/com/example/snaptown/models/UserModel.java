package com.example.snaptown.models;

public class UserModel {

	private String name;
	private String facebookId;
	private String gcmClientToken;
	private String authToken;

	public UserModel(String name, String facebookId, String gcmClientToken,
			String authToken) {
		this.name = name;
		this.facebookId = facebookId;
		this.gcmClientToken = gcmClientToken;
		this.authToken = authToken;
	}

	public String getAuthToken(){
		return this.authToken;
	}
	
	@Override
	public String toString() {
		String template = "{ \"Name\": %s, \"FacebookId\": %s, \"GCMClientToken\": %s, \"AuthToken\": %s }";
		String name = (this.name != null ? ("\"" + this.name + "\"") : null);
		String facebookId = (this.facebookId != null ? ("\"" + this.facebookId + "\"")
				: null);
		String gcmClientToken = (this.gcmClientToken != null ? ("\""
				+ this.gcmClientToken + "\"") : null);
		String authToken = (this.authToken != null ? ("\"" + this.authToken + "\"")
				: null);
		String res = String.format(template, name, facebookId, gcmClientToken,
				authToken);
		return res;
	}
}
