package com.isslam.kidsquran.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.isslam.kidsquran.model.AudioClass;
import com.isslam.kidsquran.model.Reciters;
import com.isslam.kidsquran.model.Suras;
import com.isslam.kidsquran.utils.GlobalConfig;
import com.isslam.kidsquran.utils.Utils;



public class AudioListManager {
	public static int reciterId = 1;
	public static int suraId = 78;
	public static int ayahId = 1;
	public static Boolean updatePlayer = true;
	private static AudioListManager instance = null;
	private ArrayList<AudioClass> playerAudioList = new ArrayList<AudioClass>();
	private ArrayList<AudioClass> audioList = new ArrayList<AudioClass>();
	private ArrayList<Reciters> reciters=new ArrayList<Reciters>();
	private ArrayList<Suras> suras=new ArrayList<Suras>();
	public static AudioListManager getInstance() {
		if (instance == null) {
			instance = new AudioListManager();
		}
		return instance;
	}

	public Boolean getUpdatePlayerStatus() {
		return updatePlayer;
	}

	public void setUpdatePlayerStatus(Boolean status) {
		updatePlayer = status;
	}
	

	// Constructor
	public AudioListManager() {
		
		if (sura == null) {
			sura = GlobalConfig.GetmyDbHelper().get_Sura_by_id(suraId,GlobalConfig.lang_id);

		}
		//FillRandomAudio();

	}
	
	
	public void FillRandomAudio() {
		if (playerAudioList.isEmpty()) {
			AudioClass audio = new AudioClass();
			audio.setReciterId(reciter.getId());
			audio.setReciterName(reciter.getName());
			audio.setImage(reciter.getImage());

		
		//	audio.setAudioPath(reciter.getAudioBasePath() + surasId);
			_AudioClass = audio;
			playerAudioList.add(audio);
		}
	}

	// ----------------------
	Reciters reciter = null;
	Suras sura = null;

	public Reciters getSelectedReciter() {
		return reciter;
	}
	public void setSelectedReciter(Reciters _reciter,Context _context) {
		reciter = _reciter;
		reciterId = reciter.getId();
		SharedPreferencesManager sharedPreferencesManager=SharedPreferencesManager.getInstance(_context);
		sharedPreferencesManager.savePreferences(SharedPreferencesManager._reciter_id, reciterId);
	}
	
	
	
	public void updateAllreciters(int _reciterId,Context context)
	{
		reciters= GlobalConfig.myDbHelper.get_reciters(GlobalConfig.lang_id);
		boolean found=false;
		for(int i=0;i<reciters.size();i++)
		{
			if(reciters.get(i).getId()==_reciterId)
			{
				reciter = reciters.get(i);
				reciterId = _reciterId;
				found=true;
				break;
			}
		}
		if(!found)
		{
			reciter = reciters.get(0);
			reciterId= 1;
		}
		
	}
	public void updateAllSuras()
	{
		suras = GlobalConfig.GetmyDbHelper().get_verses(GlobalConfig.lang_id);
		Log.e("suaras",suras.size()+"");
	}
	public void UpdateMemory(int sura_id,int status)
	{
		for(int i=0;i<suras.size();i++)
		{
			if(suras.get(i).getId()==sura_id)
			{
				suras.get(i).setMemorize(status);
			}
		}
		GlobalConfig.GetmyDbHelper().update_memory(
				sura_id, status);

	}
	public Suras getSuraBySuraId(int sura_id)
	{
		Log.e("sura_id",sura_id+"");
		for(int i=0;i<suras.size();i++)
		{
			Log.e("sura_id",suras.get(i).getId()+"");
			if(suras.get(i).getId()==sura_id)
			{
				return suras.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<Suras> getAllSuras() {
		return suras;
	}
	public void setAllSuras(ArrayList<Suras> _suras) {
		suras = _suras;
	}
	public void UpdateStras(int sura_id,int status)
	{
		for(int i=0;i<suras.size();i++)
		{
			if(suras.get(i).getId()==sura_id)
			{
				suras.get(i).setStars(status);
			}
		}
		GlobalConfig.GetmyDbHelper().update_stars(sura_id, status);
		

	}
	
	public void UpdateRecords(int sura_id,int status)
	{
		for(int i=0;i<suras.size();i++)
		{
			if(suras.get(i).getId()==sura_id)
			{
				suras.get(i).setRecords(status);
			}
		}
		GlobalConfig.GetmyDbHelper().update_records(sura_id, status);
		

	}
	
	
	
	public void updateSuraList()
	{
		sura = GlobalConfig.GetmyDbHelper().get_Sura_by_id(suraId,GlobalConfig.lang_id);
	}
	public Suras getSelectedSura() {
		return sura;
	}
	public void setSelectedSura(Suras _sura) {
		sura = _sura;
	}
	
	public ArrayList<Reciters> getSelectedReciters() {
		return reciters;
	}
	public void setSelectedReciters1(ArrayList<Reciters> _reciters) {
		reciters = _reciters;
	}
	

	AudioClass _AudioClass = null;

	public AudioClass getRandomAudioClass() {
		return _AudioClass;
	}

	

	// -------------

	public ArrayList<AudioClass> getPlayList() {
		return playerAudioList;
	}

	public void AddNewSura(AudioClass sura) {
		playerAudioList.add(sura);

	}

	public void AddNewSuraAt(int index, AudioClass sura) {
		playerAudioList.add(index, sura);

	}

	public void deletAllSuras() {
		playerAudioList.clear();
	}

	public void SetSongs(ArrayList<AudioClass> suras) {
		playerAudioList.clear();
		playerAudioList = suras;
	}

	public Boolean isVerseExist(int reciterId, int verseId) {
		for (int i = 0; i < playerAudioList.size(); i++) {
			if (playerAudioList.get(i).getReciterId() == reciterId
					&& playerAudioList.get(i).getVerseId() == verseId)
				return true;
		}
		return false;
	}

	public void SetaudioList(ArrayList<AudioClass> suras) {
		audioList.clear();
		audioList = suras;
	}

	public ArrayList<AudioClass> getPlayListSuras() {

		return audioList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
