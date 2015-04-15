package com.isslam.kidsquran.utils;

import com.isslam.kidsquran.MainActivity;
import com.isslam.kidsquran.R;
import com.isslam.kidsquran.model.DataBaseHelper;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class GlobalConfig extends Activity {


	
	public static String _DBcontext = "";
	public static String local = "ar";
	public static String lang_id = "1";
	public static MainActivity mainActivity = null;
	//audio
	public static String audioRootPath = "KidsQuran";
	public static String audioExtension = ".mp3";
	public static String zipExtension = ".zip";
	public static String filetocheck = "114005.mp3";
	public static int ayatCount = 608;
	
	public static DataBaseHelper myDbHelper = null;
	// log file..
	public static Boolean showLog = true;

	public static void Log(String tag, String msg) {
		if (showLog)
			try {
				android.util.Log.e(tag, msg);
			} catch (Exception e) {

			}
	}

	public static void ShowSuccessToast(Context context, String msg) {
		clearTosts();
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater1.inflate(R.layout.ly_toast, null);
		TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
		textMessage.setText(msg);
		toast.setView(view);
		toast.show();
	}

	public static void ShowErrorToast(Context context, String msg) {
		clearTosts();
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater1.inflate(R.layout.ly_toast_error, null);
		TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
		textMessage.setText(msg);
		toast.setView(view);
		toast.show();
	}

	static Toast toast = null;



	public static DataBaseHelper GetmyDbHelper() {
		try {
			if (myDbHelper == null) {
				myDbHelper = new DataBaseHelper(null);
				myDbHelper.InitDB();
			}
		} catch (Exception e) {

		}
		return myDbHelper;
	}

	public static void clearTosts() {
		if (toast != null)
			toast.cancel();
	}
	public static void ShowToast(Context context, String msg) {
		clearTosts();
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater1 = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater1.inflate(R.layout.ly_toast, null);
		TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
		textMessage.setText(msg);
		//toast.setGravity(Gravity.TOP | Gravity.LEFT | Gravity.FILL_HORIZONTAL,
			//	0, ((Activity) context).getActionBar().getHeight());

		toast.setView(view);
		toast.show();

	}
}