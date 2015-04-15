package com.isslam.kidsquran.model;

public class DownloadClass {

	private int id;
	private AudioClass audioClass;
	private int progress;

	// -----------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	// -------------
	public AudioClass getAudioClass() {
		return audioClass;
	}

	public void setAudioClass(AudioClass audioClass) {
		this.audioClass = audioClass;
	}

}