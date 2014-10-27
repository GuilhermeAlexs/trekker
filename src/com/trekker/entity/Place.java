package com.trekker.entity;

import android.location.Location;

public class Place {
	private Location location;
	private String name;
	private String description;
	private double price;
	private boolean important;

	public Place(){
		
	}
	
	public Place(Location location, String name, String description,
			double price, boolean important) {
		this.location = location;
		this.name = name;
		this.description = description;
		this.price = price;
		this.important = important;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}
}
