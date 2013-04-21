package nasa.appchallenge.smartcitiesclient.httputils;

import java.util.Map;

import android.location.Location;
import android.os.AsyncTask;

public class AsyncSendData extends AsyncTask<String, Integer, String> {

	public final static String DEVICE_ID = "1667";
	private static final String SEND_DATA_URL = "http://chemigallego.es:2013/SmartCity/PutData.aspx";
	private Map<String, String> data;
	private Location location;

	public AsyncSendData(Map<String, String> data, Location location) {
		this.data = data;
		this.location = location;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			String myXML = "<Measurements> <Measurement> <deviceId>" + DEVICE_ID
					+ "</deviceId> <longitude>" + location.getLongitude() + "</longitude> <latitude>"
					+ location.getLatitude() + "</latitude> <temperature>" + "33,33333"
					+ "</temperature> </Measurement> </Measurements>";

			new HttpUtils().makeHttpRequest(SEND_DATA_URL, myXML);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
