package nasa.appchallenge.smartcitiesclient.httputils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nasa.appchallenge.smartcitiesclient.CustomMapFragment;
import nasa.appchallenge.smartcitiesclient.MapActivity;
import nasa.appchallenge.smartcitiesclient.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncGetMeasurements extends AsyncTask<String, String, String> {

	private Activity activity;
	private Location location;
	private final static String SERVER_URL = "http://chemigallego.es:2013/SmartCity/GetData2.aspx?lat=";

	public AsyncGetMeasurements(Activity activity, Location location) {
		this.activity = activity;
		this.location = location;
	}

	@Override
	protected String doInBackground(String... url) {
		String result = null;
		try {
			String urlString = SERVER_URL + location.getLatitude() + "&lon=" + location.getLongitude();
			URL url1 = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");
			InputStream xml = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			Element rootElement = doc.getDocumentElement();

			NodeList nodes = rootElement.getChildNodes();
			Log.e("SERVER RESPONSE", "ROOT DATA:" + rootElement.getNodeName());
			Map<String, String> data = new HashMap<String, String>();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);

				if (node instanceof Element) {
					// a child element to process
					Log.e("SERVER RESPONSE", "THERE IS ONE:" + node.getNodeName());
					Element elem = (Element) node;
					String elemString = node.getTextContent();
					if (elemString != null && !elemString.isEmpty()) {
						data.put(node.getNodeName(), elemString);
						Log.e("SERVER RESPONSE", node.getNodeName() + " " + elemString);
					}
				}
			}
			((MapActivity)activity).setData(data);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result){
		((CustomMapFragment)((MapActivity)activity).getLocationFragment()).addWeatherLabel();
	}
}
