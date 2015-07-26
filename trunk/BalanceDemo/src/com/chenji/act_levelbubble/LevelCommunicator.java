package com.chenji.act_levelbubble;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import com.chenji.communication.SocketReceiver;
import com.chenji.communication.SocketSender;





import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LevelCommunicator implements Runnable{
	private Socket socket;
	private SocketSender sender;
	private SocketReceiver receiver;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private Thread sendThread;
	private Thread receiveThread;
	
	private BlockingQueue<String> sendQueue;
	private BlockingQueue<String> receiveQueue;
	private BlockingQueue<CommandTask> taskQueue;
	
	private Handler communicatorHandler;	

	
	private static final int COMMAND_TASK_QUEUE_SIZE = 2;
	private static final int SEND_RECEIVE_QUEUE_SIZE = 100;
	

	

	
	
	
	private static LevelCommunicator _instance = null;
	
	public static LevelCommunicator getLevelCommunicator(Socket socket)
	{
		if (_instance == null)
		{
			_instance = new LevelCommunicator(socket);
		}
		return _instance;
	}
	
	private static LevelCommunicator getLevelCommunicator()
	{
		assert(_instance != null);
		return _instance;
	}
	
	
	public static Handler getCommunicatorHandler()
	{
		return getLevelCommunicator().communicatorHandler;
	}
	
	private LevelCommunicator(Socket socket)
	{
		this.socket = socket;
		taskQueue = new ArrayBlockingQueue<CommandTask>(COMMAND_TASK_QUEUE_SIZE);
		sendQueue  = new ArrayBlockingQueue<String>(SEND_RECEIVE_QUEUE_SIZE);
		receiveQueue = new ArrayBlockingQueue<String>(SEND_RECEIVE_QUEUE_SIZE);

		communicatorHandler = new CommunicatorHandler(); // A Timer to control the sending of SI command
			
		
		
	}
	
	
	public boolean addTask(CommandTask commandTask)
	{
		// Only one task is available in queue, except when there is an SI task
		boolean offerSuccess;
		CommandTask top;
		
		commandTask.setInputAndOutputQueue(sendQueue, receiveQueue);
		top = taskQueue.peek();
		if (top == null) 
		{
		
			offerSuccess = taskQueue.offer(commandTask);
			if (offerSuccess)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			return false;
		}
	}
	

	

	private void disconnect()
	{
		try {
			//sendThread.interrupt();
			//receiveThread.interrupt();
			if (this.socket.isConnected())
			{

				socket.shutdownInput();
				socket.shutdownOutput();
				if (!socket.isClosed())
				{
					socket.close();
				}
				Log.i("BALANCE",""+socket.isClosed());
				socket = null;
				
			}


		} catch (java.net.SocketException e)
		{
			Log.e("BALANCE",e.toString());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.e("BALANCE",e.toString());
		}

	}
	@Override
	public void run() {
		
		
		
		try {
			
			
			this.in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();   
		}
		

		this.receiver = new SocketReceiver(this.in, (ArrayBlockingQueue<String>) receiveQueue);
		this.sender = new SocketSender(this.out, (ArrayBlockingQueue<String>) sendQueue);
		receiveThread = new Thread(this.receiver);
		sendThread = new Thread(this.sender);

		
		receiveThread.start();
		sendThread.start();
		
		try {
			
			CommandTask commandtask;
			
			while (!Thread.currentThread().isInterrupted())
			{
				commandtask = taskQueue.take();
				commandtask.process();
			}
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.i("BALANCE","INTERRUPTED_Communicator");
			sendThread.interrupt();
			receiveThread.interrupt();
		}
		finally
		{
			disconnect();
		}
		
	}
	
	private static class CommunicatorHandler extends Handler
	{
		
		
		public CommunicatorHandler()
		{
			
		}
		
		public void handleMessage(Message msg){
			
		}
	
		
	}
	
	

}


