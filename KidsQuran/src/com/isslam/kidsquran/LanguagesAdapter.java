package com.isslam.kidsquran;

import com.isslam.kidsquran.utils.GlobalConfig;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class LanguagesAdapter extends BaseAdapter {

	private Context activity;
	private String[] data;
	private static LayoutInflater inflater = null;
	private String fontPath;
	private Typeface tf;
	

	public LanguagesAdapter(Context _FragmentActivity, String[] languages) {
		activity = _FragmentActivity;
		//fontPath = GlobalConfig.fontPath;
	//	tf = Typeface.createFromAsset(activity.getAssets(), fontPath);
		data = languages;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	

	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null) {
			vi = inflater.inflate(R.layout.ly_languages_item, null);

		}
		TextView txt_playLists_name = (TextView) vi
				.findViewById(R.id.txt_playLists_name);
		// txt_playLists_name.setTypeface(tf);

		if (txt_playLists_name != null) {

			txt_playLists_name.setText(data[position]);
		}

		return vi;
	}

	private int selectedItem;

	public void setSelectedItem(int position) {
		selectedItem = position;
	}

}