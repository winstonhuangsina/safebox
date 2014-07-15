package com.safebox.msg;

import intent.pack.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class DeviceLocation extends Activity implements LocationListener {
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	Button test_btn;
	TextView test_text;
	String lat;
	String provider;
	protected String latitude, longitude;
	protected boolean gps_enabled, network_enabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testdbbybutton);
		test_btn = (Button) findViewById(R.id.test_btn);

		/*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);*/

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double latitude = location.getLatitude(); // 经度
		double longitude = location.getLongitude(); // 纬度
		double altitude = location.getAltitude(); // 海拔
		Log.v("tag", "latitude " + latitude + "  longitude:" + longitude
				+ " altitude:" + altitude);
		test_text = (TextView) findViewById(R.id.test_text);
		test_text.setText("Latitude:" + location.getLatitude() + ", Longitude:"
				+ location.getLongitude());
	}

	@Override
	public void onLocationChanged(Location location) {
		//test_text = (TextView) findViewById(R.id.test_text);
		test_text.setText("Latitude:" + location.getLatitude() + ", Longitude:"
				+ location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("Latitude", "disable");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("Latitude", "enable");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("Latitude", "status");
	}
}