/**
 * GPIO Control class for Linux/Android
 * 
 * Copyright (C) 2012 ogutti
 * 
 * @author t.ogura@gmail.com (ogutti)
 * 
 * new BSD License
 */
package com.ogutti.android.gpiotest;

import java.io.FileWriter;
import java.io.IOException;

/**
 * GPIO Control class for Linux/Android
 */
public class GpioWriter {
	/** 
	 * gpio pin no
	 */
	private int pin_;
	
	/**
	 * FileWriter for Linux/Android device file
	 */
	private FileWriter file_;
	
	/**
	 * default GPIO device path
	 */
	final private String kDevicePath = "/sys/devices/virtual/gpio/";
	
	/** 
	 * Constructor with pin number
	 * @param pin GPIO pin number
	 */
	public GpioWriter(int pin) {
		this.pin_ = pin;
	}
	
	/**
	 * Open device file with the device path
	 * @throws IOException
	 */
	public void open() throws IOException{
		file_ = new FileWriter(this.getDevicePath());
	}

	/**
	 * Generate full path for GPIO device file
	 * @return full path of GPIO device file
	 */
	public String getDevicePath() {
		return kDevicePath + "gpio" + pin_ + "/direction";
	}
	
	/**
	 * write high or low to GPIO pin
	 * @param is_on if true, write high value, else low
	 * @throws IOException
	 */
	
	public void write(boolean is_on) throws IOException {
		if (file_ != null) {
			if (is_on) {
				file_.write("high");
			} else {
				file_.write("low");
			}
		}
	}
	
	/**
	 * closes the device file
	 * 
	 * it ignores exception
	 */

	public void close() {
		try {
			if (file_ != null) {
				file_.close();
			}
		} catch (IOException e) {
		}
	}
	
	/**
	 * check if the device file is opened
	 * @return true means opened (ready for write)
	 */

	public boolean isOpened() {
		return (file_ != null);
	}
	
	/**
	 * close the device file if it is opened yet
	 */

	@Override
	protected void finalize() throws Throwable {
		try{
			super.finalize();
		}finally{
			this.close();
		}
	}
}
