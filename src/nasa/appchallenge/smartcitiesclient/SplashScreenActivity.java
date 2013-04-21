package nasa.appchallenge.smartcitiesclient;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIME = 2000;
	private static final String TAG = "SplashScreenActivity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen_layout);
		Handler handler = new Handler();
		createHttpCache();
		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isNetworkConnected()) {
					RelativeLayout introLayout = (RelativeLayout) findViewById(R.id.splashscreen);
					Animation fadeOutAnim = AnimationUtils.loadAnimation(SplashScreenActivity.this, android.R.anim.fade_out);
					introLayout.startAnimation(fadeOutAnim);
					introLayout.setVisibility(View.GONE);
					
					finish();
					Intent intent = new Intent(SplashScreenActivity.this, MapActivity.class);
					SplashScreenActivity.this.startActivity(intent);
				}
				else
				{
					Toast.makeText(SplashScreenActivity.this, "Please connect to the Internet and try again.", Toast.LENGTH_LONG).show();
				}
			}
		}, SPLASH_TIME);
	}

	   private void createHttpCache() {
			try {
		           File httpCacheDir = new File(getApplication().getCacheDir(), "http");
		           long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
		           Class.forName("android.net.http.HttpResponseCache")
		                   .getMethod("install", File.class, long.class)
		                   .invoke(null, httpCacheDir, httpCacheSize);}
		        catch (Exception httpResponseCacheNotAvailable) {
		        	Log.e(TAG, "HttpResponseCache Not Available");
		       }
	}

	@Override
	    public void onAttachedToWindow() {
	    	getWindow().setFormat(PixelFormat.RGBA_8888);
	    }
	
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return (ni != null);
	}
}