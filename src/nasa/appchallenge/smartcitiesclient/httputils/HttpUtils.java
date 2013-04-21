package nasa.appchallenge.smartcitiesclient.httputils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class HttpUtils {

	private static final String TAG = "HttpUtils";
	String pass;
	BigInteger hash;
	StringBuilder sb;
	public InputStream bis;

	public String makeHttpRequest(String url, String xml) throws ServerException {
		String result = null;
		url = url.replaceAll(" ", "%20");
		try {
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(url);
//			if (namevaluepair != null)
//				httppost.setEntity(new UrlEncodedFormEntity(namevaluepair));
//			HttpResponse response = httpclient.execute(httppost);
			HttpPost httppost = new HttpPost(url);          
			StringEntity se = new StringEntity(xml ,HTTP.UTF_8);

			se.setContentType("text/xml");  
			httppost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
			httppost.setEntity(se);  

			HttpClient httpclient = new DefaultHttpClient();
			BasicHttpResponse httpResponse = 
			    (BasicHttpResponse) httpclient.execute(httppost);
			HttpEntity entity = httpResponse.getEntity();
			bis = entity.getContent();
			result = readResponse();

		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Encoding is not supported");
			e.printStackTrace();
			throw new ServerException();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Client Protocol Exception");
			e.printStackTrace();
			throw new ServerException();
		} catch (IOException e) {
			Log.e(TAG, "IOException thrown");
			e.printStackTrace();
			throw new ServerException();
		}
		return result;
	}

	private String readResponse() throws UnsupportedEncodingException, IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(bis, writer, "UTF-8");
		bis.close();
		String result = writer.toString();
		Log.e(TAG, "SERVER RESULT:" + result + "-");

		return result;
	}

	public String makeHttpRequest(String url) throws ServerException {
		return makeHttpRequest(url, null);
	}
}
