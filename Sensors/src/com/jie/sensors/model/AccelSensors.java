package com.jie.sensors.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class AccelSensors implements SensorEventListener {
	private Activity context;
	
	public boolean hasSensor(){
		return getAccelorometer() != null;
	}
	
	private Handler accelEventHandler;
	
	public AccelSensors(Activity context){
		this.context = context;
	}
	
	public void setSensorChangedHandler(Handler handler){
		this.accelEventHandler = handler;
	}
	
	private List<Sensor> getAccelorometer() {
		SensorManager mngr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> list = mngr.getSensorList(Sensor.TYPE_ACCELEROMETER);
		return list != null && !list.isEmpty() ? list : null;
	}

	public void registerListener() {
		SensorManager mngr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> list = getAccelorometer();
		if(list != null) {
			for(Sensor sensor: list) {
				mngr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
			}
		}
	}

	public void unregisterListener() {
		if(hasSensor()) {
			SensorManager mngr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			mngr.unregisterListener(this);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { }

	@Override
	public void onSensorChanged(SensorEvent event) {
		float ax = event.values[0];
		float ay = event.values.length > 1 ? event.values[1] : 0;
		float az = event.values.length > 2 ? event.values[2] : 0;

		Bundle data = new Bundle();
		data.putString("x", "X: "+ax+" m/s^2");
		data.putString("y", "Y: "+ay+" m/s^2");
		data.putString("z", "Z: "+az+" m/s^2");
		Message msg = Message.obtain();
		msg.setData(data);
		if(accelEventHandler != null){
			accelEventHandler.sendMessage(msg);
		}
	}
	
}
