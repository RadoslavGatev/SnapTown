package com.example.snaptown.models;

public class Town {
	public int townId;
	public String name;
	public String country;
	public boolean isSubscribed;

	public Town(int townId, String name, String country, boolean isSubscribed) {
		this.townId = townId;
		this.name = name;
		this.country = country;
		this.isSubscribed = isSubscribed;
	}

	public CharSequence getName() {
		return name;
	}

	public CharSequence getCountry() {
		return country;
	}

}
