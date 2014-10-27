package com.trekker.service;

import com.trekker.entity.ChangeLocationListener;
import com.trekker.entity.Map;
import com.trekker.entity.Place;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class MapService extends Service implements LocationListener {
    private final IBinder mBinder = new LocalBinder();
    private Location location = null;
    private ChangeLocationListener listener;
    private static final long MIN_DISTANCE = 5;
    private static final long MIN_TIME = 3200;
    private LocationManager locationManager;
    private String currentProvider = null;
    private boolean locationHasChanged = false;
    private boolean isMapping = false;
    private Map map;

    public MapService(){
    }
    
    @Override
    public void onCreate() {
    	map = new Map();
    	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    	startGPS();
    }    

    @Override
	public void onDestroy() {
    	super.onDestroy();
        stopGPS();
    }

    public class LocalBinder extends Binder {
    	public MapService getService() {
            return MapService.this;
        }
    }

    public Location getLocation() {
    	if(currentProvider == null)
    		return null;

    	if(location == null)
    		return locationManager.getLastKnownLocation(currentProvider);

        return location;
    }

    public void setLocation(Location location){
    	if(isBetterLocation(location,this.location)){
    		this.location = location;
    		this.locationHasChanged = true;
    	}
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MIN_TIME;
        boolean isSignificantlyOlder = timeDelta < -MIN_TIME;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer)
            return true;
        else if (isSignificantlyOlder)
            return false;

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }

        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null)
          return provider2 == null;

        return provider1.equals(provider2);
    }

    public void startMapping(){
    	isMapping = true;
    }
    
    public void stopMapping(){
    	isMapping = false;
    }
    
    public boolean isMapping(){
    	return isMapping;
    }
    
    public void startGPS(){
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE, this);
            this.currentProvider = LocationManager.GPS_PROVIDER;
        }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DISTANCE, this);
	        this.currentProvider = LocationManager.NETWORK_PROVIDER;
        }
    }

    public void stopGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(MapService.this);
        }
    }
    
    public void addPlace(Place place){
    	map.addPlace(place);
    }

    @Override
    public void onLocationChanged(Location location) {
    	setLocation(location);
    	Location newLoc = getLocation();
    	if(newLoc != null && isMapping && this.locationHasChanged){
    		map.addPoint(location);
    		if(listener != null) 
    			listener.onChangeLocation(this.map.getPath());
    		this.locationHasChanged = false;
    	}
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void setChangeLocationListener(ChangeLocationListener listener){
    	this.listener = listener;
    }

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}