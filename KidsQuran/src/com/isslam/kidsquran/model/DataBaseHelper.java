package com.isslam.kidsquran.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.isslam.kidsquran.Albumb;
import com.isslam.kidsquran.DBActivity;
import com.isslam.kidsquran.R;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;

public class DataBaseHelper extends SQLiteOpenHelper {

	public interface DataBaseHelperInterface {
		public void onRequestCompleted();
	}

	private DataBaseHelperInterface mCallback;

	private static String DB_PATH = "/data/data/" + GlobalConfig._DBcontext
			+ "/databases/";
	private static String DB_NAME = "book_db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private int searccTopResult = 30;
	private static final int DATABASE_VERSION = 1;
	String myPath;

	public DataBaseHelper(DataBaseHelperInterface callback) {

		super(DBActivity.getContext(), DB_NAME, null, DATABASE_VERSION);
		this.myContext = DBActivity.getContext();
		mCallback = callback;
		myPath = myContext.getFilesDir().getAbsolutePath()
				.replace("files", "databases")
				+ File.separator + DB_NAME;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("upgrade to next level", "start");
		// if (deleteDataBase())
		// InitDB();
		try {
			copyDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (oldVersion == 1 && newVersion >= 2) {
			// execute upgrade queries
			oldVersion = 2;
		}
		if (oldVersion == 2 && newVersion >= 3) {
			// execute database upgrade queries
			oldVersion = 3;
		}
		
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			copyDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void InitDB()// Call when the application Run
	{
		try {

			boolean result = createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {

			openDataBase();

		} catch (SQLException sqle) {

			throw sqle;
		}
		if (mCallback != null)
			mCallback.onRequestCompleted();
	}

	public boolean createDataBase() throws IOException {
		myDataBase = null;
		boolean dbExist = checkDataBase();// check if we DB SQLlite Exist or not

		if (dbExist) {
			return false;
		} else {
			try {
				myDataBase = this.getReadableDatabase();
				myDataBase.close();
				copyDataBase();// //Copy the External DB to the application

				return true;
			} catch (IOException e) {
				return false;

			}
		}

	}

	public boolean checkDataBase() {
		try {
			// String myPath = DB_PATH + DB_NAME;

			File dbFile = new File(myPath);
			return dbFile.exists();
		} catch (SQLiteException e) {
		}
		return false;
	}

	public boolean deleteDataBase()// Delere on Upgrade
	{
		try {
			// String myPath = DB_PATH + DB_NAME;
			File dbFile = new File(myPath);
			if (dbFile.exists()) {
				dbFile.delete();
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		openDataBase();
		// creatVirtualDB();
	}

	public void openDataBase() throws SQLException { // Open the DB

		// Open the database
		// String myPath = DB_PATH + DB_NAME;
		// myDataBase = this.getReadableDatabase();
		if (myDataBase != null) {
			if (myDataBase.isOpen()) {
				// myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				// myDataBase.op
				myDataBase.close();
			}

		}
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	

	@Override
	public synchronized void close() {
		Log.e("close", "close DB");
		if (myDataBase != null)
			myDataBase.close();
		SQLiteDatabase db = this.getReadableDatabase();
		db.close();
		super.close();

	}

	// verses
	public ArrayList<Suras> get_verses(String langId)// get
														// allsemichapters
	{
		ArrayList<Suras> versesList = new ArrayList<Suras>();

		String selectQuery = "SELECT verses.id,verses.ayah_count,verses.place_id,verses_translation.name,verses.memory,verses.stars,verses.record FROM verses,verses_translation where lang_id ="
				+ langId + " AND verses.id = verses_translation.verses_id";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		try {
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Suras verses = new Suras();
					verses.setId(Integer.parseInt(cursor.getString(0)));
					verses.setName(cursor.getString(3));
					verses.setAyahCount(Integer.parseInt(cursor.getString(1)));
					verses.setPlaceId(Integer.parseInt(cursor.getString(2)));
					verses.setMemorize(Integer.parseInt(cursor.getString(4)));
					verses.setStars(Integer.parseInt(cursor.getString(5)));
					verses.setRecords(Integer.parseInt(cursor.getString(6)));
					versesList.add(verses);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}

		return versesList;
	}


	

	public ArrayList<Suras> get_random_verses(String langId)// get
	// allsemichapters
	{
		ArrayList<Suras> versesList = new ArrayList<Suras>();

		String selectQuery = "SELECT verses.id,verses.ayah_count,verses.place_id,verses_translation.name FROM verses,verses_translation where lang_id ="
				+ langId
				+ " AND verses.id = verses_translation.verses_id ORDER BY RANDOM() LIMIT 1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {
					Suras verses = new Suras();
					verses.setId(Integer.parseInt(cursor.getString(0)));
					verses.setName(cursor.getString(3));
					verses.setAyahCount(Integer.parseInt(cursor.getString(1)));
					verses.setPlaceId(Integer.parseInt(cursor.getString(2)));
					versesList.add(verses);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return versesList;
	}

	public int get_memory(int id) {

		String selectQuery = "SELECT verses.memory FROM verses where verses.id="
				+ id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int memory = 0;
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					memory = (Integer.parseInt(cursor.getString(0)));

				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return memory;
	}
	public int get_record(int id) {

		String selectQuery = "SELECT verses.record FROM verses where verses.id="
				+ id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int record = 0;
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					record = (Integer.parseInt(cursor.getString(0)));

				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return record;
	}
	public void update_memory(int id, int memory) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("memory", memory);
		db.update("verses", values, " id = '" + id + "'", null);

	}
	public void update_stars(int id, int stars) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("stars", stars);
		db.update("verses", values, " id = '" + id + "'", null);

	}
	public void update_records(int id, int record) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("record", record);
		db.update("verses", values, " id = '" + id + "'", null);

	}
	
	
	
	public void update_gift(int cat_id,int item_id, int status) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("status", status);
		db.update("gifts", values, " cat_id = '" + cat_id + "' AND  item_id = '" + item_id + "'", null);

	}

	public Suras get_Sura_by_id(int id, String langId) {

		String selectQuery = "SELECT verses.id,verses.ayah_count,verses.place_id,verses_translation.name,verses.memory,verses.stars,verses.record FROM verses,verses_translation where lang_id ="
				+ langId
				+ " AND verses.id="
				+ id
				+ " AND verses.id = verses_translation.verses_id";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Suras suras = new Suras();
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					suras.setId(Integer.parseInt(cursor.getString(0)));
					suras.setName(cursor.getString(3));
					suras.setAyahCount(Integer.parseInt(cursor.getString(1)) + 1);
					suras.setPlaceId(Integer.parseInt(cursor.getString(2)));
					suras.setMemorize(Integer.parseInt(cursor.getString(4)));
					suras.setStars(Integer.parseInt(cursor.getString(5)));
					suras.setRecords(Integer.parseInt(cursor.getString(6)));
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return suras;
	}

	public ArrayList<Tafseer> get_tafseer_by_id(int sura_id, int ayah_id) {
		String selectQuery = "SELECT title,content FROM tafseer where verses_id ="
				+ sura_id + " AND ayah_id=" + ayah_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<Tafseer> tafseerList = new ArrayList<Tafseer>();
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {
					Tafseer tafseer = new Tafseer();
					tafseer.setTitle(cursor.getString(0));
					tafseer.setContent(cursor.getString(1));
					tafseerList.add(tafseer);

				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return tafseerList;
	}

	// verses
	public Reciters get_reciters_by_id(int id, String langId)// get
	// allsemichapters
	{

		String selectQuery = "SELECT reciters.id,reciters.image,reciters.country_id,reciters.position,reciters.status,reciters_translation.name,reciters.audio_base_path FROM reciters,reciters_translation where lang_id ="
				+ langId
				+ " AND reciters.id="
				+ id
				+ " AND reciters.id = reciters_translation.reciter_id";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Reciters reciters = new Reciters();
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					reciters.setId(Integer.parseInt(cursor.getString(0)));
					reciters.setName(cursor.getString(5));
					reciters.setImage(cursor.getString(1));
					reciters.setCountryId(Integer.parseInt(cursor.getString(2)));
					reciters.setOrder(Integer.parseInt(cursor.getString(3)));
					reciters.setStatus(Integer.parseInt(cursor.getString(4)));
					reciters.setAudioBasePath(cursor.getString(6));

				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return reciters;
	}

	public ArrayList<Reciters> get_reciters(String langId)// get
															// allsemichapters
	{
		ArrayList<Reciters> recitersList = new ArrayList<Reciters>();
		String selectQuery = "SELECT reciters.id,reciters.image,reciters.country_id,reciters.position,reciters.status,reciters_translation.name,reciters.audio_base_path FROM reciters,reciters_translation where lang_id ="
				+ langId + " AND reciters.id = reciters_translation.reciter_id";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					Reciters reciters = new Reciters();
					reciters.setId(Integer.parseInt(cursor.getString(0)));
					reciters.setName(cursor.getString(5));
					reciters.setImage(cursor.getString(1));
					reciters.setCountryId(Integer.parseInt(cursor.getString(2)));
					reciters.setOrder(Integer.parseInt(cursor.getString(3)));
					reciters.setStatus(Integer.parseInt(cursor.getString(4)));
					reciters.setAudioBasePath(cursor.getString(6));
					recitersList.add(reciters);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return recitersList;
	}

	public ArrayList<Gifts> get_gifts()// get
	// allsemichapters
	{
		ArrayList<Gifts> giftsList = new ArrayList<Gifts>();
		String selectQuery = "SELECT cat_id,item_id,status FROM gifts";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					Gifts gifts = new Gifts();
					gifts.setCatId(Integer.parseInt(cursor.getString(0)));
					gifts.setItemId(Integer.parseInt(cursor.getString(1)));
					gifts.setStatus(Integer.parseInt(cursor.getString(2)));
				
					giftsList.add(gifts);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return giftsList;
	}
	public ArrayList<AwardsOrder> get_awards_orders(int id)// get
	// allsemichapters
	{
		ArrayList<AwardsOrder> awardsOrderList = new ArrayList<AwardsOrder>();
		String selectQuery = "SELECT * FROM awards_orders where id="+id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					AwardsOrder awardsOrder = new AwardsOrder();
					awardsOrder.setId(Integer.parseInt(cursor.getString(0)));
					awardsOrder.setCatId(Integer.parseInt(cursor.getString(1)));
					awardsOrder.setItemId(Integer.parseInt(cursor.getString(2)));
					awardsOrder.setSuraId(Integer.parseInt(cursor.getString(3)));
					awardsOrder.setAyahId(Integer.parseInt(cursor.getString(4)));
					awardsOrder.setReciterId(Integer.parseInt(cursor.getString(5)));
					
					awardsOrderList.add(awardsOrder);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return awardsOrderList;
	}
	
	public ArrayList<Gifts> get_gifts_by_id(int cat_id)// get
	// allsemichapters
	{
		ArrayList<Gifts> giftsList = new ArrayList<Gifts>();
		String selectQuery = "SELECT cat_id,item_id,status FROM gifts where cat_id = "+cat_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					Gifts gifts = new Gifts();
					gifts.setCatId(Integer.parseInt(cursor.getString(0)));
					gifts.setItemId(Integer.parseInt(cursor.getString(1)));
					gifts.setStatus(Integer.parseInt(cursor.getString(2)));
				
					giftsList.add(gifts);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return giftsList;
	}

	public ArrayList<Reciters> get_reciters_folders(String langId,
			List<Albumb> list)// get
	// allsemichapters
	{

		String idQuery = "";
		for (int i = 0; i < list.size(); i++) {
			if (i == 0)
				idQuery = idQuery + list.get(i).getName();
			else
				idQuery = idQuery + "," + list.get(i).getName();
		}
		ArrayList<Reciters> recitersList = new ArrayList<Reciters>();
		String selectQuery = "SELECT reciters.id,reciters.image,reciters.country_id,reciters.position,reciters.status,reciters_translation.name,reciters.audio_base_path FROM reciters,reciters_translation where lang_id ="
				+ langId
				+ " AND reciters.id IN ("
				+ idQuery
				+ ") "
				+ " AND reciters.id = reciters_translation.reciter_id";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		try {
			if (cursor.moveToFirst()) {
				do {

					Reciters reciters = new Reciters();
					reciters.setId(Integer.parseInt(cursor.getString(0)));
					reciters.setName(cursor.getString(5));
					reciters.setImage(cursor.getString(1));
					reciters.setCountryId(Integer.parseInt(cursor.getString(2)));
					reciters.setOrder(Integer.parseInt(cursor.getString(3)));
					reciters.setStatus(Integer.parseInt(cursor.getString(4)));
					reciters.setAudioBasePath(cursor.getString(6));
					reciters.setItems(getFoldersLength(list,
							cursor.getString(0)));
					recitersList.add(reciters);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return recitersList;
	}

	private String getFoldersLength(List<Albumb> list, String reciter_id) {

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getName().equals(reciter_id)) {
				Log.e("folder", list.get(i).getData() + " : " + reciter_id);
				return list.get(i).getData();
			}
		}
		return "0";

	}

	public ArrayList<Reciters> get_random_reciters(String langId)// get
	// allsemichapters
	{
		ArrayList<Reciters> recitersList = new ArrayList<Reciters>();
		String selectQuery = "SELECT reciters.id,reciters.image,reciters.country_id,reciters.position,reciters.status,reciters_translation.name,reciters.audio_base_path FROM reciters,reciters_translation where lang_id ="
				+ langId
				+ " AND reciters.id = reciters_translation.reciter_id ORDER BY RANDOM() LIMIT 1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		try {
			if (cursor.moveToFirst()) {
				do {

					Reciters reciters = new Reciters();
					reciters.setId(Integer.parseInt(cursor.getString(0)));
					reciters.setName(cursor.getString(5));
					reciters.setImage(cursor.getString(1));
					reciters.setCountryId(Integer.parseInt(cursor.getString(2)));
					reciters.setOrder(Integer.parseInt(cursor.getString(3)));
					reciters.setStatus(Integer.parseInt(cursor.getString(4)));
					reciters.setAudioBasePath(cursor.getString(6));
					recitersList.add(reciters);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
			db.close();
		}
		return recitersList;
	}

	// PlayLists
	

	
	
}