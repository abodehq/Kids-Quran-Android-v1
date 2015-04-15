package com.isslam.kidsquran.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.isslam.kidsquran.model.AudioClass;
import com.isslam.kidsquran.model.AwardsOrder;
import com.isslam.kidsquran.model.Gifts;
import com.isslam.kidsquran.model.Reciters;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;



public class GiftsManager {

	private static GiftsManager instance = null;
	
	private ArrayList<Reciters> reciters=new ArrayList<Reciters>();
	public static GiftsManager getInstance() {
		if (instance == null) {
			instance = new GiftsManager();
		}
		return instance;
	}

	Reciters reciter;
	// Constructor
	public GiftsManager() {
	

	}
	int last_awards_order_id = -1;
	ArrayList<AwardsOrder> awardsOrderList = null;
	public ArrayList<AwardsOrder> GetAwardsList(Context context)
	{
		SharedPreferencesManager sharedPreferencesManager=SharedPreferencesManager.getInstance(context);
		int id =sharedPreferencesManager.GetIntegerPreferences(SharedPreferencesManager._award_order, 1);
		if(id!=last_awards_order_id||awardsOrderList==null)
		{
			
			awardsOrderList = GlobalConfig.myDbHelper.get_awards_orders(id);
			last_awards_order_id = id;
		}
		Log.e("--gift: ", id + " : size -> "+awardsOrderList.size());
		return awardsOrderList;
	}
	
	ArrayList<Gifts> gifts;
	int sura_Id = 0;
	int ayah_counts = 4;
	public void randomize()
	{
		int sura_id = 1;
		int ayahcounts = 0;
		if(gifts==null)
			gifts=GlobalConfig.myDbHelper.get_gifts();
		int counts = 0;
		for(int i =0;i<gifts.size();i++)
		{
			if(gifts.get(i).getStatus()==1)
			{
				counts++;
			}
		}
		int factor1= (counts%5)+2 ;
		Random random = new Random();
		int show= random.nextInt(factor1);
		if(show==1)
		{
			 int cat_id = random.nextInt(7)+1;
		     int item_id = random.nextInt(6)+1;
		     if(counts<20)
		     {
		    	 cat_id = random.nextInt(5)+1;
			     item_id = random.nextInt(4)+1;
		     }
		     Gifts gift = null;
		     for(int i =0;i<gifts.size();i++)
			{
	    	   gift =  gifts.get(i);
				if(gift.getCatId()==cat_id&&gift.getItemId()==item_id)
				{
					break;
				}
			}
		     if(gift!=null)
		     {
		    	 if(gift.getCount() == 8 )
		    		 show = 0; 
		     }
		}
		
	}
	public ArrayList<Gifts> getGifts() {
		gifts= GlobalConfig.myDbHelper.get_gifts();
		return gifts;
	}
	public ArrayList<Gifts> getGiftsById(int id) {
		ArrayList<Gifts> gifts= GlobalConfig.myDbHelper.get_gifts_by_id(id);
		return gifts;
	}
	
	
}
