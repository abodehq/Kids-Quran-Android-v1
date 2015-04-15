package com.isslam.kidsquran.model;

public class Suras {

	private int id;
	private String name;
	private int ayahCount;
	private int placeId;
	private String audioPath;
	private float size;
	private int memorize;
	private int stars;
	private int records;
	// -----------
		public int getMemorize() {
			return memorize;
		}

		public void setMemorize(int memorize) {
			this.memorize = memorize;
		}
		// -----------
		public int getStars() {
			return stars;
		}

		public void setStars(int stars) {
			this.stars = stars;
		}
		// -----------
		public int getRecords() {
			return records;
		}

		public void setRecords(int records) {
			this.records = records;
		}
	// -----------
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	// -----------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// -------------
	public int getAyahCount() {
		return ayahCount;
	}

	public void setAyahCount(int ayahCount) {
		this.ayahCount = ayahCount;
	}

	// -------------
	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

}