package com.jie.sensors.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import com.jie.sensors.model.*;
import com.jie.sensors.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SensorMonitor extends Activity{
	private static final String TAG = SensorMonitor.class.getName();
    private String FILENAME;
    private final String SEPARATER = System.getProperty("line.separator");

	private final AccelSensors accelSensors = new AccelSensors(this);
	private final LinearAccelSensors linearAccelSensors = new LinearAccelSensors(this);
	private final LocationSensors locationSensors = new LocationSensors(this);

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar c = Calendar.getInstance(); 
        int seconds = c.get(Calendar.SECOND);
        FILENAME = seconds + "data.txt";
        
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
				writeToFile(data.getString("latitude") + "/" + data.getString("longitude"));
				writeToFile(SEPARATER);
				}
			});
		setContentView(R.layout.activity_sensor_monitor);
		Button stopButton = (Button) findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});
//		setTitle("Sensor Monitor");
	}
	
	private void sendMessage() {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(this, StoredData.class);
		myIntent.putExtra("data", readFromFile());
		startActivity(myIntent);
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
	
	private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        } 
    }
 
    private ArrayList<String> readFromFile() {
         
    	ArrayList<String> ret = new ArrayList<String>();
         
        try {
            InputStream inputStream = openFileInput(FILENAME);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    ret.add(receiveString);
                }
                 
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }
        
        return ret;
    }
}
