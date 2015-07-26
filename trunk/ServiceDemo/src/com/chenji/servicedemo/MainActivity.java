package com.chenji.servicedemo;

import com.example.servicedemo.R;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button Start;
	Button End;
	TextView tv_phoneNumber;
	TextView tv_message;
	
	SMSService.SMSBinder binder;
	SmsReceiver smsReceiver;
	
	private int ServiceCounter = 0;
	
	private ServiceConnection servConn = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder servBinder)
		{
			Log.i("SMS","--service connected--");
			binder = (SMSService.SMSBinder) servBinder;
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("SMS","--service disconnected--");
			
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Start = (Button)findViewById(R.id.startService);
		End = (Button)findViewById(R.id.endService);
		tv_phoneNumber = (TextView)findViewById(R.id.phonenumber);
		tv_message = (TextView)findViewById(R.id.message);
		tv_phoneNumber.setText("__________");
		tv_message.setText("__________");
		
		final Intent servIntent = new Intent();
		servIntent.setAction("com.chenji.servicedemo.SMS_SERVICE");
		
		smsReceiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, filter);
		
		Start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Log.i("SMS1", "Start is cliecked");
				if (ServiceCounter < 1)
				{
					bindService(servIntent, servConn, Service.BIND_AUTO_CREATE);
					ServiceCounter++;
				}
				
			}
			
		});
		
		End.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.i("SMS1", "End is cliecked");
				if (ServiceCounter>=1)
				{
					unbindService(servConn);
					ServiceCounter--;
				}
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
						
						tv_phoneNumber.setText(message.getDisplayOriginatingAddress());
						
						
						tv_message.setText(message.getDisplayMessageBody());
	
						

						
					}
				}
			}
			
		}
		
	}

}
