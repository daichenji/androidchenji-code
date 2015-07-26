package com.chenji.act_levelbubble;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import android.os.Handler;
import android.os.Message;


abstract public class CommandTask{
	
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	
	public static final int SI_TIMEOUT = 2000; //ms
	
	public static final int CMD_TIMEOUT = 1000;
	public static final int STABILITY_TIMEOUT = 300000;
	
	private String command;
	private Pattern pattern;
	protected Handler commandHandler;
	
	protected Object commandResult;
	
	protected BlockingQueue<String> sendQueue;
	protected BlockingQueue<String> receiveQueue;
	
	public CommandTask(Handler commandHandler, BlockingQueue<String> sendQueue, BlockingQueue<String> receiveQueue)
	{
		this(commandHandler);
		setInputAndOutputQueue(sendQueue, receiveQueue);
	}
	
	public CommandTask(Handler commandHandler)
	{
		this.commandHandler = commandHandler;
	}
	
	public final void setInputAndOutputQueue(BlockingQueue<String> sendQueue, BlockingQueue<String> receiveQueue)
	{
		this.sendQueue = sendQueue;
		this.receiveQueue = receiveQueue;
	}
	
	public final String getCommandToSend(){
		return command;
	}

	public final boolean isMatch(String responseStr)
	{
		return pattern.matcher(responseStr).matches();
	}
	
	public final void processSuccess()
	{
		if (commandHandler==null) return;
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = commandResult;
		commandHandler.sendMessage(msg);
	}
	
	public final void processFailed()
	{
		if (commandHandler==null) return;
		Message msg = new Message();
		msg.what = FAILED;
		msg.obj = null;
		commandHandler.sendMessage(msg);
	}
	
	public abstract boolean process() throws InterruptedException;

	

}
