package com.jie.sensors.model;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LocationSensors implements LocationListener{

	private Activity context;
	
	public LocationSensors(Activity context){
		this.context = context;
	}
	
	private Handler locationChangedEventHandler;
	
	public void setSensorChangedHandler(Handler handler){
		this.locationChangedEventHandler = handler;
		registerListener();
	}

	public void registerListener() {
		LocationManager mngr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mngr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.01f, this);
	}

	public void unregisterListener() {
		LocationManager mngr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		mngr.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		double altitude = location.getAltitude();

		Bundle data = new Bundle();
		data.putString("longitude", "Longitude: "+longitude);
		data.putString("latitude", "Latitude: "+latitude);
		data.putString("altitude", "Altitude: "+altitude);
		Message msg = Message.obtain();
		msg.setData(data);
		if(locationChangedEventHandler != null){
			locationChangedEventHandler.sendMessage(msg);
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
