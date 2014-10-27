package com.trekker;

public enum MarkerType {
	PLACE_BEGIN (R.drawable.begin),
	PLACE_FINISH (R.drawable.finish),
	PLACE_INTERESTING (R.drawable.location);
	
	private int id;
	
	MarkerType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
}
