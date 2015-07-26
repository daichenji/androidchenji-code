package com.chenji.communication;

import java.io.BufferedReader;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import com.chenji.util.Logger;

public class SocketReceiver implements Runnable {

	private BufferedReader sockerReader = null;
	
	private ArrayBlockingQueue<String> receiveQueue;
	
	public SocketReceiver(BufferedReader in, ArrayBlockingQueue<String> receiveQueue) 
	{
		
		this.sockerReader = in;
		this.receiveQueue = receiveQueue;
	}
	

	@Override
	public void run() {
		
					
			try {
				while(!Thread.currentThread().isInterrupted())
				{				
					String receive = sockerReader.readLine();
					
					receiveQueue.put(receive);
					
				};

			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
}
