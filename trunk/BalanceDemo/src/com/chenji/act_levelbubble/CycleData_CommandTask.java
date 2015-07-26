package com.chenji.act_levelbubble;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






import android.os.Handler;



public class CycleData_CommandTask extends CommandTask{
	
	public CycleData_CommandTask(Handler commandHandler) {
		super(commandHandler);
		// TODO Auto-generated constructor stub
	}

	private final Pattern pattern = Pattern.compile("(\\-?\\d+\\.?\\d*)[ ]+((-?\\d+\\.?\\d*))");
	
			
	public boolean process() throws InterruptedException {			
	
		
		
		//No send, only Receive
		while (true)
		{
			String responseStr = receiveQueue.poll(CommandTask.CMD_TIMEOUT, TimeUnit.MILLISECONDS); 

			
			if (responseStr==null){ // timeout, no reply
				processFailed();
				
			}

			Matcher m = pattern.matcher(responseStr);
			if (!m.find())
			{
				continue;
			}
			
			// get command parameters here
			String radiumStr = m.group(1);
			String angelStr = m.group(2);

			
			
			double radium = Double.parseDouble(radiumStr);
			double angel = Double.parseDouble(angelStr);

			commandResult = new CycleData(radium,angel);
			processSuccess();
			
			
		}
		
	}
	
	public class CycleData
	{
		private double radium;
		private double angel;
		
		CycleData(double radium, double angel)
		{
			this.radium = radium;
			this.angel = angel;
		}
		
		public double getRadium()
		{
			return this.radium;
		}
		
		public double getAngel()
		{
			return this.angel;
		}
	}
}