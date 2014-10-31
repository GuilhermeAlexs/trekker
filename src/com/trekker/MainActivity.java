package com.trekker;

import java.util.List;

import com.trekker.entity.ChangeLocationListener;
import com.trekker.service.MapService;
import com.trekker.service.MapService.LocalBinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity implements ChangeLocationListener,
		OnClickListener {
	public static final int PLACE_BEGIN = R.drawable.begin;
	public static final int PLACE_FINISH = R.drawable.finish;
	public static final int PLACE_INTERESTING = R.drawable.location;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawerLinear;
	private ListView mDrawerListView;

	private BlackMapView mapView;
	private MapService mapService;
	private boolean mBound = false;
	private ImageView newLocBtn;
	private Activity activity;
	private MarkerOptionsAdapter adapter;
	private String[] values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		activity = MainActivity.this;
		values = new String[] { "food", "landscape" };
		adapter = new MarkerOptionsAdapter(this, values);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLinear = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerListView = (ListView) findViewById(R.id.drawer_listview);
		
		mapView = (BlackMapView) findViewById(R.id.mapView);
		newLocBtn = (ImageView) findViewById(R.id.placeBtn);

		mDrawerLayout.closeDrawer(mDrawerLinear);
		prepareDrawer();
		
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		mDrawerListView.setAdapter(adapter);

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
		switch (v.getId()) {
		case R.id.placeBtn:
			// if(!mapService.isMapping()){
			// mapService.startMapping();
			// mapView.addMarker(mapService.getLocation(), PLACE_BEGIN);
			// }else{
			// mapService.stopMapping();
			// mapView.addMarker(mapService.getLocation(), PLACE_FINISH);
			// }
			// break;
			showMarkersOptionsDialog();
		}
	}

	public void prepareDrawer() {
		mDrawerToggle = new ActionBarDrawerToggle(activity, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
	}

	public void showMarkersOptionsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Choose the type of Marker");
		builder.setAdapter(adapter, null).setPositiveButton(
				android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();
		ListView markersList = alertDialog.getListView();
		markersList.setAdapter(adapter);
		markersList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		markersList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
		alertDialog.show();
	}
}
