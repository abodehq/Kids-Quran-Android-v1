package com.isslam.kidsquran.model;

public class AwardsOrder {

	private int id;
	private int cat_id;
	private int item_id;
	private int sura_id;
	private int ayah_id;
	private int reciter_id;
	// -----------
		public int getReciterId() {
			return reciter_id;
		}

		public void setReciterId(int reciter_id) {
			this.reciter_id = reciter_id;
		}
	// -----------
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	// -----------
	public int getAyahId() {
		return ayah_id;
	}

	public void setAyahId(int ayah_id) {
		this.ayah_id = ayah_id;
	}
	// -----------
	public int getCatId() {
		return cat_id;
	}

	public void setCatId(int cat_id) {
		this.cat_id = cat_id;
	}
	// -----------
	public int getItemId() {
		return item_id;
	}

	public void setItemId(int item_id) {
		this.item_id = item_id;
	}
	// -----------
	public int getSuraId() {
		return sura_id;
	}

	public void setSuraId(int sura_id) {
		this.sura_id = sura_id;
	}


}