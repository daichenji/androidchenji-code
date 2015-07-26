package com.chenji.commands;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.chenji.act_levelbubble.CommandTask;
import com.chenji.util.Unit;
import com.chenji.util.Weight;

import android.os.Handler;



public class SI_CommandTask extends CommandTask{
	
	public SI_CommandTask(Handler commandHandler) {
		super(commandHandler);
		// TODO Auto-generated constructor stub
	}

	private final Pattern pattern = Pattern.compile("S[ ]+([S|D])[ ]+(\\d+(\\.\\d+)?)[ ]+([a-z]+)");
	private final String command=  "SI";	
			
	public boolean process() throws InterruptedException {			
		boolean success;	
		//Send
		success = sendQueue.offer(this.command, CommandTask.SI_TIMEOUT, TimeUnit.MILLISECONDS);
				
		if (!success)
		{
			processFailed();
			return false;
		}
		
		// Receive
		while (true)
		{
			String responseStr = receiveQueue.poll(CommandTask.SI_TIMEOUT, TimeUnit.MILLISECONDS); 
			
			
			if (responseStr==null){ // timeout, no reply
				processFailed();
				return false;
			}

			Matcher m = pattern.matcher(responseStr);
			if (!m.find())
			{
				continue;
			}
			
			// get command parameters here
			String stableStr = m.group(1);
			String weight = m.group(2);
			String unit = m.group(4);
			
			boolean isStable = stableStr.equals("S")?true:false;
			double weightvalue = Double.parseDouble(weight);

			commandResult = new Weight(weightvalue,Unit.getUnitByName(unit),isStable);
			processSuccess();
			
			return true;
		}
	}
}