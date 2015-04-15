package com.isslam.kidsquran.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {

	private static SharedPreferencesManager instance = null;
	private Context context;

	public static String _local = "ar";
	public static String _lang_id = "1";
	public static String _full_mode = "_full_mode";
	public static String _first_run = "_first_run";
	public static String _no_ads = "_no_ads";
	public static String _sound = "_sound";
	public static String _award_order = "_award_order";
	public static String _reciter_id = "_reciter_id";
	public static SharedPreferencesManager getInstance(Context _context) {
		if (instance == null) {
			instance = new SharedPreferencesManager();
		}
		instance.setContext(_context);
		return instance;
	}

	private void setContext(Context _context) {
		context = _context;
	}

	public void SetupPreferences() {

	}

	public Boolean getBooleanPreferences(String key, Boolean defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getBoolean(key, defaultValue);
		}
		return false;
	}

	public String GetStringPreferences(String key, String defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getString(key, defaultValue);
		}
		return "";
	}

	public int GetIntegerPreferences(String key, int defaultValue) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			return sharedPreferences.getInt(key, defaultValue);
		}
		return -1;
	}

	public void savePreferences(String key, String value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}

	}

	public void savePreferences(String key, boolean value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}

	}

	public void savePreferences(String key, int value) {
		if (context != null) {
			SharedPreferences sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			Editor editor = sharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}

	}

}
