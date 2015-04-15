package com.isslam.kidsquran.model;

public class AudioClass {

	private int verseId;
	private String verseName;
	private int ayahCount;
	private int placeId;
	private String audioPath;
	private int reciterId;
	private String reciterName;
	private String image;

	public String getVerseName() {
		return verseName;
	}

	public void setVerseName(String verseName) {
		this.verseName = verseName;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	// -----------
	public int getVerseId() {
		return verseId;
	}

	public void setVerseId(int verseId) {
		this.verseId = verseId;
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

	// reciters
	// ------------
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	// ----------------
	public String getReciterName() {
		return reciterName;
	}

	public void setReciterName(String reciterName) {
		this.reciterName = reciterName;
	}

	// -----------
	public int getReciterId() {
		return reciterId;
	}

	public void setReciterId(int reciterId) {
		this.reciterId = reciterId;
	}

}