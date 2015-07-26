package com.chenji.act_balance;
import java.lang.ref.WeakReference;
import java.net.Socket;

import com.chenji.act_levelbubble.CommandTask;
import com.chenji.act_levelbubble.CommandTaskHandler;
import com.chenji.balance.R;
import com.chenji.commands.T_CommandTask;
import com.chenji.commands.Z_CommandTask;
import com.chenji.util.Weight;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class BasicWeighingActivity extends Activity {
	
	private TextView tv_main_text;	
	private Button bn_menu;
	private Button bn_cal;
	private Button bn_app;
	private Button bn_power;
	private Button bn_tare;
	private Button bn_zero;	
	
	

	private BalanceCommunicator communicator = null;
	private Thread communicatorThread = null;
	
	private static Socket socket = null;
	
	static final int DISP = 0x01;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weighing);
		
		tv_main_text = (TextView) findViewById(R.id.tv_main_text);
		bn_tare = (Button) findViewById(R.id.bn_tare);
		
		bn_menu = (Button) findViewById(R.id.bn_menu);
		bn_cal = (Button) findViewById(R.id.bn_calibration);
		bn_app = (Button) findViewById(R.id.bn_app);
		bn_power = (Button) findViewById(R.id.bn_power);
		bn_tare = (Button) findViewById(R.id.bn_tare);
		bn_zero = (Button) findViewById(R.id.bn_zero);

		Handler viewHandler = new ViewHandler(this);
    
        communicator = BalanceCommunicator.getBalanceCommunicator(socket,viewHandler);
        communicatorThread = new Thread(this.communicator);
        
        bn_tare.setOnClickListener(
        		new OnClickListener(){
        			@Override
        			public void onClick(View v) {
        				
        				communicator.addTask(new T_CommandTask(new TCommandHanlder()));
        				tv_main_text.setText("--------");
        				
        				}
        		}        			
        		);
        
        bn_zero.setOnClickListener(
        		new OnClickListener(){
        			@Override
        			public void onClick(View v) {
        				
        				communicator.addTask(new Z_CommandTask(new ZCommandHanlder()));
        				tv_main_text.setText("--------");
        				
        				}
        		}        			
        		);
	}

	
	protected void onResume()
	{
		super.onResume();		
		communicatorThread.start();
	}

	
	protected void onPause()
	{
		communicatorThread.interrupt();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_weighing, menu);
		return true;
	}

	static public void setSocket(Socket socket)
	{
		BasicWeighingActivity.socket = socket;
	}
	
	private Object getViewObject(int index)
	{
		return findViewById(index);
	}
	
	static class ViewHandler extends Handler
	{
		private final WeakReference<Activity> mActivity;
		
		public ViewHandler(Activity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}
	
		public void handleMessage(Message msg){
			DispMessageObject displayMessage = (DispMessageObject) msg.obj;
			String content;
			
			content = displayMessage.getContent(DispMessageObject.MAIN);
			if (content != null){
				((TextView) ((BasicWeighingActivity) mActivity.get()).getViewObject(R.id.tv_main_text)).setText(content);
				}
			
			content = displayMessage.getContent(DispMessageObject.ICONS);
			if (content != null){
				//displayer.tv_main_text.setText(msg.obj.toString());
				}
			}
	}
	
	static class TCommandHanlder extends CommandTaskHandler
	{

		@Override
		public void handleCommand(Message msg) {
			Message viewMsg = new Message();
			DispMessageObject dispObj = new DispMessageObject();
			
			if (msg.what == CommandTask.SUCCESS)
			{					
				Weight weight = (Weight) msg.obj;										
				dispObj.addContent(DispMessageObject.MAIN,""+weight.getWeight()+" "+weight.getUnit().getName());	
			}
			else
			{
				dispObj.addContent(DispMessageObject.MAIN,"NULL");
				
			}
			viewMsg.obj = dispObj;
			BalanceCommunicator.getViewHandler().sendMessage(viewMsg);			
		}
	}
	
	static class ZCommandHanlder extends CommandTaskHandler
	{

		@Override
		public void handleCommand(Message msg) {
			Message viewMsg = new Message();
			DispMessageObject dispObj = new DispMessageObject();
			
			if (msg.what == CommandTask.SUCCESS)
			{					
				
			}
			else
			{
				dispObj.addContent(DispMessageObject.MAIN,"NULL");
				
			}
			viewMsg.obj = dispObj;
			BalanceCommunicator.getViewHandler().sendMessage(viewMsg);			
		}
	}
}
