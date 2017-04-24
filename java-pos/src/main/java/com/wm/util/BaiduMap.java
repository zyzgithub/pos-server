package com.wm.util;

public class BaiduMap {
	private int status;
	private Result result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public class Result {
		private Location location;
		private int precise;
		private int confidence;
		private String level;
		
		public Result() {}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}
		
		public int getPrecise() {
			return precise;
		}

		public void setPrecise(int precise) {
			this.precise = precise;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}
		
		public class Location {
			private double lng;
			private double lat;
			
			public Location() {}

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

		}
	}

	
}
