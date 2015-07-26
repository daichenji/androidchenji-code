package com.chenji.act_levelbubble;


import com.chenji.act_levelbubble.LevelCommunicator;

import android.os.Handler;
import android.os.Message;

public abstract class CommandTaskHandler extends Handler{

	
	public CommandTaskHandler()
	{
		super();
	}
	
	public void handleMessage(Message msg)
	{

		Message msgForCommunicator = new Message();
		msgForCommunicator.copyFrom(msg);
		LevelCommunicator.getCommunicatorHandler().sendMessage(msgForCommunicator);

		

		handleCommand(msg);

	}
	
	public abstract void handleCommand(Message msg);
	
}
