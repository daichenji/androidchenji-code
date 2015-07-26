package com.chenji.servicedemo;

import com.chenji.servicedemo.MainActivity.SmsReceiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;



public class SMSService extends Service
{
	
	private SmsManager smsManager;
	
	private SMSBinder binder = new SMSBinder();
	private SmsReceiver smsReceiver;
	IntentFilter receiveFilter;
	
	public class SMSBinder extends Binder
	{
		
	}
	
	
	@Override
	public IBinder onBind(Intent intent)
	{
		System.out.println("Service is Binded");
		registerReceiver(smsReceiver, receiveFilter);

		return binder;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		smsReceiver = new SmsReceiver();
		receiveFilter = new IntentFilter();
		receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		
		
		System.out.println("Service is Created");
		
		
			
	}
	
	@Override
	public boolean onUnbind(Intent intent)
	{
		System.out.println("Service is Unbinded");
		unregisterReceiver(smsReceiver);
		return true;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		
		System.out.println("Service is Destroyed");
	}
	
	@Override
	public void onRebind(Intent intent) 
	{
		super.onRebind(intent);
		
		System.out.println("Service is ReBinded");
	}
	
	public class SmsReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
			{
				StringBuilder sb = new StringBuilder();
				Bundle bundle = intent.getExtras();
				if (bundle != null)
				{
					Object[] pdus = (Object[]) bundle.get("pdus");
					SmsMessage[] messages = new SmsMessage[pdus.length];
					for (int i=0;i<pdus.length;++i)
					{
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						
					}
					for (SmsMessage message:messages)
					{
						sb.append("From:\n");
						sb.append(message.getDisplayOriginatingAddress()+"\n");						
						
						sb.append("Message:\n");
						sb.append(message.getDisplayMessageBody());						
						
						 
						Log.i("SMS1",sb.toString());
						
						String msgtext = message.getDisplayMessageBody();
						
						// Show in Toast if the message contains "ÄãºÃ"
						if (msgtext.contains("ÄãºÃ"))
						{
							Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
						}
						
						
					}
				}
			}
		}
	}

}
