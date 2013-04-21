package nasa.appchallenge.smartcitiesclient;

import java.util.Map;
import java.util.Map.Entry;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMapFragment extends MapFragment implements OnInfoWindowClickListener {
	private LatLng mPosFija;
	private Map<String, String> conditions;
	private Marker marker;

	public CustomMapFragment() {
		super();

	}

	public static Fragment newInstance(LatLng position) {
		CustomMapFragment frag = new CustomMapFragment();
		frag.mPosFija = position;
		return (Fragment) frag;
	}

	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View v = super.onCreateView(arg0, arg1, arg2);
		initMap();
		return v;
	}

	private void initMap() {
		CameraPosition cameraPosition = new CameraPosition.Builder().target(mPosFija).zoom(17).bearing(0).tilt(60)
				.build();

		GoogleMap map = getMap();
		if (map != null) {
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			map.setMyLocationEnabled(true);

		}
	}

	public void updateCoords(Location location) {
		mPosFija = new LatLng(location.getLatitude(), location.getLongitude());
		marker.setPosition(mPosFija);
	}

	public void addWeatherLabel() {
		conditions = ((MapActivity)getActivity()).getData();
		StringBuilder weather = new StringBuilder();
		int markerId = R.drawable.some_clouds;;
		for(Entry<String, String> entry : conditions.entrySet()){
			if(entry.getKey().equals("Temperature"))
			{
				if(Integer.parseInt(entry.getValue())<10)
					markerId = R.drawable.cloud;
					
				else if(Integer.parseInt(entry.getValue())>=10 && Integer.parseInt(entry.getValue())<20)
					markerId = R.drawable.some_clouds;
				
				else if(Integer.parseInt(entry.getValue())>=20)
					markerId = R.drawable.sunny;
			}
			weather = weather.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		GoogleMap map = getMap();
		map.setInfoWindowAdapter(new PopupAdapter(getActivity().getLayoutInflater()));
	      map.setOnInfoWindowClickListener(this);
		marker = map.addMarker(new MarkerOptions().position(mPosFija).title("Weather Conditions").snippet(weather.toString()).icon(BitmapDescriptorFactory.fromResource(markerId)));
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_LONG).show();

	}
}
