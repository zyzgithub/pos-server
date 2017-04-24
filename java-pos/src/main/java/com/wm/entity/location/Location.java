package com.wm.entity.location;

public class Location {

	private double lng;
	private double lat;
	private String address;
	private String city;

	public Location(double lng, double lat, String address, String city) {
		super();
		this.lng = lng;
		this.lat = lat;
		this.address = address;
		this.city = city;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

}
