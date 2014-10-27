package com.trekker.entity;

import java.util.LinkedList;
import java.util.List;

import android.location.Location;

public class Map {
	private List<Location> path;
	private List<Place> places;

	public Map(){
		this.path = new LinkedList<Location>();
	}

	public void addPoint(Location location){
		path.add(location);
	}

	public void addPlace(Place place){
		places.add(place);
	}

	public List<Location> getPath(){
		return this.path;
	}
}
