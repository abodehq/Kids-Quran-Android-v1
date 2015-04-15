package com.isslam.kidsquran.model;

public class Tafseer {

	private int id;
	private String title;
	private String content;
	
	private int sura_id;
	private int ayah_id;
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	// -----------
	public int getSuraId() {
		return sura_id;
	}

	public void setSuraId(int sura_id) {
		this.sura_id = sura_id;
	}
	
	// -----------
	public int getAyahId() {
		return ayah_id;
	}

	public void setAyahId(int ayah_id) {
		this.ayah_id = ayah_id;
	}

	

}