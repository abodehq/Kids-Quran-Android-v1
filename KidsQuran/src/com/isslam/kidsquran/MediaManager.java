package com.isslam.kidsquran;

import java.io.IOException;

import com.isslam.kidsquran.controllers.AudioListManager;
import com.isslam.kidsquran.controllers.SharedPreferencesManager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class MediaManager {

	private static MediaManager instance = null;
	private Context context;
	private boolean isPrepared = false;
	MediaPlayer mp = new MediaPlayer();

	public MediaManager() {
		mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				isPrepared = true;
			}
		});

	}

	public static MediaManager getInstance(Context _context) {
		if (instance == null) {
			instance = new MediaManager();
		}
		instance.setContext(_context);
		return instance;
	}

	public long getDuration() {
		return mp.getDuration();
	}

	public Boolean getIsprepared() {

		return isPrepared;
	}

	public long getCurrentPosition() {
		return mp.getCurrentPosition();
	}

	public void playSounds(int position, int type) {
		if (mp == null) {
			mp = new MediaPlayer();
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					isPrepared = true;
				}
			});
		}
		if (mp.isPlaying())
			mp.stop();
		try {

			mp.reset();
		} catch (Exception e) {

		}
		try {
			SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager
					.getInstance(context);
			int sound = sharedPreferencesManager.GetIntegerPreferences(
					SharedPreferencesManager._sound, 2);
			if (sound != 0) {
				AssetFileDescriptor afd;
				String mp3Path = "";
				if (type == 0)
					mp3Path = "mp3titles/f" + sound + "/title_" + position
							+ ".mp3";
				else
					mp3Path = "mp3/f" + sound + "/title_" + position + ".mp3";
				String fileName = position + "";
				if (fileName.length() == 2)
					fileName = "0" + fileName;
				if (fileName.length() == 1)
					fileName = "00" + fileName;

				String suraId = AudioListManager.suraId + "";
				if (suraId.length() == 2)
					suraId = "0" + suraId;
				if (suraId.length() == 1)
					suraId = "00" + suraId;

				mp3Path = "sounds/1/" + suraId + fileName + ".mp3";

				afd = context.getAssets().openFd(mp3Path);
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				mp.prepare();
				mp.start();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void StopSounds() {
		if (mp == null) {
			mp = new MediaPlayer();
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					isPrepared = true;
				}
			});
		}
		if (mp.isPlaying())
			mp.stop();
		try {

			mp.reset();
		} catch (Exception e) {

		}
	}

	public void setContext(Context _context) {
		context = _context;
	}

}
