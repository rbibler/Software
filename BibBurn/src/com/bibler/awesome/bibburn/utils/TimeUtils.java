package com.bibler.awesome.bibburn.utils;

public class TimeUtils {
	
	public static String millisToMinutes(long millis, boolean showMillis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		millis = millis % 1000;
		if(showMillis) {
			return String.format("%02d:%02d.%02d", minute, second, millis);
		} else {
			return String.format("%02d:%02d", minute, second);
		}
		
	}
	
	public static String millisToHours(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		millis = millis % 1000;
		return String.format("%02d:%02d:%02d.%02d", hour, minute, second, millis);
		
	}

}
