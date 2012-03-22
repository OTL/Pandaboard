/**
 * GPIO control Activity for Android
 * 
 * Copyright (C) 2012 ogutti
 * 
 * @author t.ogura@gmail.com (ogutti)
 * 
 * new BSD License
 */
package com.ogutti.android.gpiotest;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.AlertDialog;
import com.ogutti.android.gpiotest.GpioWriter;

/**
 * GPIO control Activity for Android
  */
public class GpioActivity extends Activity {
	
	/**
	 * creates event listeners for ToggleButton and TextView
	 * 
	 * @param button_id ToggleButton id for enabling/disabling GPIO
	 * @param text_id TextView id for pin number
	 */
	public void initPinSetting(int button_id, int text_id) {
		ToggleButton tb1 = (ToggleButton) findViewById(button_id);
		TextView text1 = (TextView) findViewById(text_id);
		tb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			/** textview for getting pin number when button is pushed */
			private TextView view_;
			
			/**
			 * set TextView object
			 * @param view
			 * @return this
			 */
			public OnCheckedChangeListener setTextView(TextView view) {
				this.view_ = view;
				return this;
			}
			
			/**
			 * callback on pushed
			 */
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int pin_no = Integer.parseInt(view_.getText().toString());
				Log.i("GPIO", "setting PIN=" + pin_no + " val=" + isChecked);
				GpioWriter gpio = new GpioWriter(pin_no);
				try {
					gpio.open();
					gpio.write(isChecked);
				} catch (IOException e) {
					/*
					 * show error message when it failed to access device file
					 */
					new AlertDialog.Builder(GpioActivity.this)
					.setTitle("GPIO Error")
					.setMessage("Did you do\n" +
							"\"echo " + pin_no + "> /sys/class/gpio/export\"\n" + 
							"and\n" + "\"chmod 666 " +
							gpio.getDevicePath() + "\"\n by su ?\n" +
							"Please use adb shell or some root tool.")
							.setPositiveButton("Ok", null)
							.show();
					buttonView.setChecked(!isChecked);
				} finally {
					gpio.close();
				}
			}
		}.setTextView(text1));
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.initPinSetting(R.id.gpioButton1, R.id.pinText1);
		this.initPinSetting(R.id.gpioButton2, R.id.pinText2);
	}
}
