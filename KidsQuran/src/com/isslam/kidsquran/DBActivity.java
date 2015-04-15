package com.isslam.kidsquran;

import android.app.Application;
import android.content.Context;

public class DBActivity extends Application {
	private static DBActivity instance;

	public DBActivity() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}
}
