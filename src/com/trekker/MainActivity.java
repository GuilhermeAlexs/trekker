package com.trekker;

import java.util.List;

import com.trekker.entity.ChangeLocationListener;
import com.trekker.service.MapService;
import com.trekker.service.MapService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity implements ChangeLocationListener, OnClickListener{
	public static final int PLACE_BEGIN = R.drawable.begin;
	public static final int PLACE_FINISH = R.drawable.finish;
	public static final int PLACE_INTERESTING = R.drawable.location;

	private BlackMapView mapView;
	private MapService mapService;
	private boolean mBound = false;
	private ImageView newLocBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mapView = (BlackMapView)findViewById(R.id.mapView);
		newLocBtn = (ImageView)findViewById(R.id.placeBtn);
		newLocBtn.setClickable(true);
		newLocBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
    protected void onResume() {
	    super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MapService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
    	super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mapService = binder.getService();
            mBound = true;
            mapService.setChangeLocationListener(MainActivity.this);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
    };

	@Override
	public void onChangeLocation(List<Location> path) {
		mapView.addPath(path);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.placeBtn:
				if(!mapService.isMapping()){
					mapService.startMapping();
					mapView.addMarker(mapService.getLocation(), PLACE_BEGIN);
				}else{
					mapService.stopMapping();
					mapView.addMarker(mapService.getLocation(), PLACE_FINISH);
				}
				break;
		}
	}
}
