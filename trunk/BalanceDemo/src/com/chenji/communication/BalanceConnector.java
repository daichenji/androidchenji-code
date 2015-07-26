package com.chenji.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class BalanceConnector implements Runnable {
	private Socket socket=null;
	private Handler connect_handler = null;
	private String ip = null;
	private int port = 65535;
	
	public BalanceConnector(Handler connect_handler) 
	{
		this.connect_handler = connect_handler;
		this.socket = new Socket();
	}
	
	
	public void connect(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
		this.socket = new Socket();
		new Thread(this).start();	
		
	}
	

	
	public Socket getSocket()
	{
		return this.socket;
	}

	

	@Override
	public void run() {
		
		Boolean connected = false;
		try {
            socket.connect(new InetSocketAddress(this.ip, this.port), 10000); // Trying connecting for 10 s
			connected = true;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.e("BALANCE",e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("BALANCE",e.toString());
		}
		
		Message msg = new Message();
		msg.what = 1;
		
		if (connected)
			msg.obj = 1;
		else
			msg.obj = 0;
		

		this.connect_handler.sendMessage(msg);
		
		
		

		
	}
}
