package com.chenji.util;

import android.util.Log;

public class Logger {
	private static String TAG = "BALANCE";
	public static void i(String logtext)
	{
		Log.i(TAG,logtext);
	}
	
	public static void mark(int number)
	{
		Logger.i(""+number);
	}
	
	public static void e(String logtext)
	{
		Log.e(TAG,logtext);
	}
	
	public static void input(String logtext)
	{
		Log.i(TAG,">>> "+logtext);
	}
	
	public static void output(String logtext)
	{
		Log.i(TAG,"<<< "+logtext);
	}
}
