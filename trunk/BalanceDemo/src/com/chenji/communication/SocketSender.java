package com.chenji.communication;


import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;

public class SocketSender implements Runnable {


	private PrintWriter sockerWriter = null;
	private ArrayBlockingQueue<String> sendQueue = null;
	
	private String END_OF_LINE = "\r\n";
	
	public SocketSender(PrintWriter sockerWriter,ArrayBlockingQueue<String> sendQueue) 
	{
		
		this.sendQueue = sendQueue;
		this.sockerWriter = sockerWriter;

		
	}
	



	@Override
	public void run() {	
			String tosend;
			try {
				while (!Thread.currentThread().isInterrupted())
				{
					
					tosend = sendQueue.take()+ END_OF_LINE; // If the queue is empty, block
					sockerWriter.print(tosend);
					sockerWriter.flush();

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
}