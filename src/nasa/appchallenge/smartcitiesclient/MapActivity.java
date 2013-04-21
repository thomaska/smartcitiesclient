package nasa.appchallenge.smartcitiesclient;

import java.util.Map;

import nasa.appchallenge.smartcitiesclient.httputils.AsyncGetMeasurements;
import nasa.appchallenge.smartcitiesclient.httputils.AsyncSendData;
import nasa.appchallenge.smartcitiesclient.locationutils.LocationResultListener;
import nasa.appchallenge.smartcitiesclient.locationutils.LocationService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity implements LocationResultListener {

	private static final String MAP_FRAGMENT_TAG = "map";
	private LocationService mLocationService;
	private Location location;
	private Fragment locationFragment;
	private Map<String,String> data;
	private MenuItem sendData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// restore last known address
		if (savedInstanceState != null)
			location = savedInstanceState.getParcelable("last_known_address");

		if (mLocationService == null)
			mLocationService = new LocationService();
		mLocationService.getLocation(this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		sendData = menu.add(R.string.send_data);
		sendData.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.equals(sendData)) {
			new AsyncSendData(data, location).execute();
			return true;
		}
		return false;
	}
	
	@Override
	public void onLocationResultAvailable(Location location) {
		this.location = location;
		if (location == null) {
			// TODO: notify user
		} else {
			setLocationFragment(getFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG));

			if (getLocationFragment() == null)
				setLocationFragment(CustomMapFragment.newInstance(new LatLng(location.getLatitude(), location
						.getLongitude())));
			else
				((CustomMapFragment)getLocationFragment()).updateCoords(location);
			getFragmentManager().beginTransaction().add(R.id.fragmentcontainer, getLocationFragment()).commit();
		}
		new AsyncGetMeasurements(MapActivity.this, location).execute();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (location != null)
			outState.putParcelable("last_known_address", location);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("No", null).show();
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
	public Map<String, String> getData() {
		return data;
	}

	public Fragment getLocationFragment() {
		return locationFragment;
	}

	public void setLocationFragment(Fragment locationFragment) {
		this.locationFragment = locationFragment;
	}
}
