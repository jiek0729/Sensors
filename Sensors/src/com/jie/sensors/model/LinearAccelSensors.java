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

public class LinearAccelSensors implements SensorEventListener {
	private Activity context;
	
	public boolean hasSensor(){
		return getLinearAcelSensors() != null;
	}
	
	private Handler linearEventHandler;
	
	public LinearAccelSensors(Activity context){
		this.context = context;
	}
	
	public void setSensorChangedHandler(Handler handler){
		this.linearEventHandler = handler;
	}
	
	private List<Sensor> getLinearAcelSensors() {
		SensorManager mngr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> list = mngr.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
		return list != null && !list.isEmpty() ? list : null;
	}

	public void registerListener() {
		SensorManager mngr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> list = getLinearAcelSensors();
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
		float lax = event.values[0];
		float lay = event.values.length > 1 ? event.values[1] : 0;
		float laz = event.values.length > 2 ? event.values[2] : 0;

		Bundle data = new Bundle();
		data.putString("x", "Linear Accel X: "+lax+" m/s^2");
		data.putString("y", "Linear Accel Y: "+lay+" m/s^2");
		data.putString("z", "Linear Accel Z: "+laz+" m/s^2");
		Message msg = Message.obtain();
		msg.setData(data);
		if(linearEventHandler != null){
			linearEventHandler.sendMessage(msg);
		}
	}

}
