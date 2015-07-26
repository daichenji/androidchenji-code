package com.chenji.act_balance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;



import com.chenji.act_levelbubble.CommandTask;
import com.chenji.act_levelbubble.CommandTaskHandler;
import com.chenji.commands.SI_CommandTask;
import com.chenji.communication.SocketReceiver;
import com.chenji.communication.SocketSender;

import com.chenji.util.Logger;
import com.chenji.util.Weight;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BalanceCommunicator implements Runnable{
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
	private Handler viewHandler;
	
	
	private static final int MSG_SI_SYNCHONIZE = 0x0;
	
	private static final int COMMAND_TASK_QUEUE_SIZE = 2;
	private static final int SEND_RECEIVE_QUEUE_SIZE = 100;
	
	private static final int SYNC_SI_DELAY_TIME = 200;
	

	
	
	
	private static BalanceCommunicator _instance = null;
	
	public static BalanceCommunicator getBalanceCommunicator(Socket socket, final Handler viewHandler)
	{
		if (_instance == null)
		{
			_instance = new BalanceCommunicator(socket,  viewHandler);
		}
		return _instance;
	}
	
	private static BalanceCommunicator getBalanceCommunicator()
	{
		assert(_instance != null);
		return _instance;
	}
	
	public static Handler getViewHandler()
	{
		return getBalanceCommunicator().viewHandler;
	}
	
	public static Handler getCommunicatorHandler()
	{
		return getBalanceCommunicator().communicatorHandler;
	}
	
	private BalanceCommunicator(Socket socket, final Handler viewHandler)
	{
		this.socket = socket;
		taskQueue = new ArrayBlockingQueue<CommandTask>(COMMAND_TASK_QUEUE_SIZE);
		sendQueue  = new ArrayBlockingQueue<String>(SEND_RECEIVE_QUEUE_SIZE);
		receiveQueue = new ArrayBlockingQueue<String>(SEND_RECEIVE_QUEUE_SIZE);

		this.viewHandler = viewHandler;
		communicatorHandler = new CommunicatorHandler(); // A Timer to control the sending of SI command
			
		
		
	}
	
	
	public boolean addTask(CommandTask commandTask)
	{
		// Only one task is available in queue, except when there is an SI task
		boolean offerSuccess;
		CommandTask top;
		
		commandTask.setInputAndOutputQueue(sendQueue, receiveQueue);
		top = taskQueue.peek();
		if ((top == null) || (top instanceof SyncSI_Command))
		{
		
			offerSuccess = taskQueue.offer(commandTask);
			if (offerSuccess)
			{
				communicatorHandler.removeMessages(MSG_SI_SYNCHONIZE);
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

			communicatorHandler.sendEmptyMessageDelayed(MSG_SI_SYNCHONIZE, SYNC_SI_DELAY_TIME);
			
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
		private CommandTask SyncSI;
		
		public CommunicatorHandler()
		{
			super();
			SyncSI = new SyncSI_Command();
		}
		
		public void handleMessage(Message msg){
			if (msg.what  == MSG_SI_SYNCHONIZE) // the message to sync SI
			{
				BalanceCommunicator.getBalanceCommunicator().addTask(SyncSI);
			}
			else // get reply from other command, start sync SI
			{
				this.sendEmptyMessageDelayed(MSG_SI_SYNCHONIZE, SYNC_SI_DELAY_TIME);
			}
		}
	
		
	}
	
	

}

final class SyncSI_Command extends SI_CommandTask{
	
	public SyncSI_Command()
	{
		super(new CommandTaskHandler()
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
			
		});
		// TODO Auto-generated constructor stub
	}}
