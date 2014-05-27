package com.jie.sensors.ui;

import com.jie.sensors.model.*;
import com.jie.sensors.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class SensorMonitor extends Activity{

	private AccelSensors accelSensors = new AccelSensors(this);
	
	private LinearAccelSensors linearAccelSensors = new LinearAccelSensors(this); 
	
	private LocationSensors locationSensors = new LocationSensors(this);

//	private final Handler accelEventHandler = new Handler() {
//								@Override
//								public void handleMessage(Message msg) {
//									Bundle data = msg.getData();
//									((TextView) findViewById(R.id.accelxtext)).setText(data.getString("x"));
//									((TextView) findViewById(R.id.accelytext)).setText(data.getString("y"));
//									((TextView) findViewById(R.id.accelztext)).setText(data.getString("z"));
//								}
//							};
//							
//	private final Handler linearEventHandler = new Handler() {
//								@Override
//								public void handleMessage(Message msg) {
//									Bundle data = msg.getData();
//									((TextView) findViewById(R.id.linearxtext)).setText(data.getString("x"));
//									((TextView) findViewById(R.id.linearytext)).setText(data.getString("y"));
//									((TextView) findViewById(R.id.linearztext)).setText(data.getString("z"));
//									}
//								};
//								
//	private final Handler locationEventHandler = new Handler() {
//								@Override
//								public void handleMessage(Message msg) {
//									Bundle data = msg.getData();
//									((TextView) findViewById(R.id.longitudetext)).setText(data.getString("longitude"));
//									((TextView) findViewById(R.id.latitudetext)).setText(data.getString("latitude"));
//									((TextView) findViewById(R.id.altitudetext)).setText(data.getString("altitude"));
//									}
//								};

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!linearAccelSensors.hasSensor() && !accelSensors.hasSensor()) {
			Toast.makeText(this, "No Accelerometer Available", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

        accelSensors.setSensorChangedHandler(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				((TextView) findViewById(R.id.accelxtext)).setText(data.getString("x"));
				((TextView) findViewById(R.id.accelytext)).setText(data.getString("y"));
				((TextView) findViewById(R.id.accelztext)).setText(data.getString("z"));
			}
		});
        linearAccelSensors.setSensorChangedHandler(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				((TextView) findViewById(R.id.linearxtext)).setText(data.getString("x"));
				((TextView) findViewById(R.id.linearytext)).setText(data.getString("y"));
				((TextView) findViewById(R.id.linearztext)).setText(data.getString("z"));
				}
			});
        locationSensors.setSensorChangedHandler(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				((TextView) findViewById(R.id.longitudetext)).setText(data.getString("longitude"));
				((TextView) findViewById(R.id.latitudetext)).setText(data.getString("latitude"));
				((TextView) findViewById(R.id.altitudetext)).setText(data.getString("altitude"));
				}
			});
		setContentView(R.layout.activity_sensor_monitor);
//		setTitle("Sensor Monitor");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(accelSensors.hasSensor()) accelSensors.registerListener();
		if(linearAccelSensors.hasSensor()) linearAccelSensors.registerListener();
		locationSensors.registerListener();
	}

	@Override
	protected void onPause() {
		super.onPause();
		accelSensors.unregisterListener();
		linearAccelSensors.unregisterListener();
		locationSensors.unregisterListener();
	}
}
