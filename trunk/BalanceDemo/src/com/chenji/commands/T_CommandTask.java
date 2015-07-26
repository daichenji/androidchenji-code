package com.chenji.commands;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;

import com.chenji.act_levelbubble.CommandTask;
import com.chenji.util.Unit;
import com.chenji.util.Weight;

public class T_CommandTask extends CommandTask{
	
	private final Pattern pattern = Pattern.compile("T[ ]+S[ ]+(\\d+(\\.\\d+)?)[ ]+([a-z]+)");
	private final String command=  "T";
	
	public T_CommandTask(Handler commandHandler) {
		super(commandHandler);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean process() throws InterruptedException {
		boolean success;	
		//Send
		success = sendQueue.offer(this.command, CommandTask.CMD_TIMEOUT, TimeUnit.MILLISECONDS);
				
		if (!success)
		{
			processFailed();
			return false;
		}
		
		// Receive
		while (true)
		{
			String responseStr = receiveQueue.poll(CommandTask.STABILITY_TIMEOUT, TimeUnit.MILLISECONDS); 
	
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
			
			String weight = m.group(1);
			String unit = m.group(3);
			
			double weightvalue = Double.parseDouble(weight);

			commandResult = new Weight(weightvalue,Unit.getUnitByName(unit),true);
			processSuccess();
			
			return true;
		}
	}

}
