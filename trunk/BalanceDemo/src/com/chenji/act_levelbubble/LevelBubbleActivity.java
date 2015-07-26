package com.chenji.act_levelbubble;


import java.lang.ref.WeakReference;
import java.net.Socket;


import com.chenji.act_levelbubble.CycleData_CommandTask.CycleData;
import com.chenji.balance.R;




import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager;

import android.widget.LinearLayout;



public class LevelBubbleActivity extends Activity {
	private static Socket socket = null;
	private LevelCommunicator communicator;
	private Thread communicatorThread;

	
	

	
	private BubbleView bubbleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  


		setContentView(R.layout.activity_level_bubble);
		
		LinearLayout root = (LinearLayout) findViewById(R.id.root);

		
		bubbleView = new BubbleView(this);
		bubbleView.setMinimumWidth(500);
		bubbleView.setMinimumHeight(300);
		
        communicator = LevelCommunicator.getLevelCommunicator(socket);
        communicatorThread = new Thread(this.communicator);
        
        
        communicator.addTask(new CycleData_CommandTask(new CycleData_CommandHanlder(this)));
        
        root.addView(bubbleView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_bubble, menu);
		return true;
	}

	
	protected void onResume()
	{

		super.onResume();		
		communicatorThread.start();
	}

	
	protected void onPause()
	{
		//communicatorThread.interrupt();
		super.onPause();
	}
	
	static public void setSocket(Socket socket)
	{
		LevelBubbleActivity.socket = socket;
	}
	
	private void UpdateCycle(CycleData data)
	{
		
		bubbleView.update(data.getRadium(), data.getAngel());
	}
		
	
	
	static class CycleData_CommandHanlder extends CommandTaskHandler
	{
		
		
		private final WeakReference<Activity> mActivity;
			
		public CycleData_CommandHanlder(Activity activity) {
			super();
			mActivity = new WeakReference<Activity>(activity);
			
		}

		@Override
		public void handleCommand(Message msg) {
			
			if (msg.what == CommandTask.SUCCESS)
			{					
				CycleData cycle = (CycleData) msg.obj;										
				((LevelBubbleActivity) mActivity.get()).UpdateCycle(cycle);
			}
			else
			{
				
				
			}
			
		}
	}	

}
