package com.isslam.kidsquran.model;

public class Reciters {

	private int id;
	private String name;
	private String image;
	private int order;
	private int status;
	private int countryId;
	private String audioBasePath;
	private String items;

	// -----------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// ------------
	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	// ------------
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	// ------------
	public String getAudioBasePath() {
		return audioBasePath;
	}

	public void setAudioBasePath(String audioBasePath) {
		this.audioBasePath = audioBasePath;
	}

	// ----------------
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// -------------
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	// -------------
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// -------------
	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}