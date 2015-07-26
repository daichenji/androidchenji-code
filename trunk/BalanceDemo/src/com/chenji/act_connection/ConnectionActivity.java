package com.chenji.act_connection;

import java.net.Socket;

import com.chenji.act_balance.BasicWeighingActivity;
import com.chenji.act_levelbubble.LevelBubbleActivity;
import com.chenji.act_manual.BalanceManual;
import com.chenji.balance.R;
import com.chenji.communication.BalanceConnector;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConnectionActivity extends Activity {

	
	Button bn_connect;
	Button bn_manual;
	TextView tv_ip;
	EditText et_ip;
	TextView tv_port;
	EditText et_port;
	TextView tv_message;
	
	RadioButton rb_basicweighing;
	RadioButton rb_levelbubble;
	
	BalanceConnector balance_connector;
	
	Boolean connected = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecotr);
        
        tv_ip = (TextView)findViewById(R.id.tv_ip);
        et_ip = (EditText)findViewById(R.id.et_ip);
        tv_port = (TextView)findViewById(R.id.tv_port);
        et_port = (EditText)findViewById(R.id.et_port);
        tv_message = (TextView)findViewById(R.id.tv_message);
        bn_connect = (Button)findViewById(R.id.bn_connect);
        rb_basicweighing = (RadioButton)findViewById(R.id.radio_basicweighing);
        rb_levelbubble = (RadioButton)findViewById(R.id.radio_levelingbubble);
        bn_manual = (Button)findViewById(R.id.bn_activate_manual_act);
        
        Handler connect_handler = new Handler()
        {
        	@Override
        	public void handleMessage(Message msg) 
        	{	

        		if (msg.obj.toString().equals("1")) // Connection is done
        		{
        			Socket socket_to_server = balance_connector.getSocket();
        			Intent intent;
        			
        			if (rb_basicweighing.isChecked())
        			{
        				// Activate Weighing with data "socket_to_server"
        				intent = new Intent(ConnectionActivity.this, BasicWeighingActivity.class);					
        				BasicWeighingActivity.setSocket(socket_to_server);
        			}
        			else if (rb_levelbubble.isChecked())
        			{
        				// Activate Weighing with data "socket_to_server"
        				intent = new Intent(ConnectionActivity.this, LevelBubbleActivity.class);					
        				LevelBubbleActivity.setSocket(socket_to_server);
        			}
        			else
        			{
        				tv_message.setText("Failed to connect!");
        				return;
        			}
					tv_message.setText("Connected");
					startActivity(intent);
					

        		}
        		else
        			tv_message.setText("Failed to connect!");
					
        	}
        };
        balance_connector = new BalanceConnector(connect_handler);
        
        bn_connect.setOnClickListener(new OnClickListener()
        {

			@Override
			public void onClick(View arg0) {
					if (!rb_basicweighing.isChecked() && !rb_levelbubble.isChecked())
					{
						return;
					}
					String ip_address = et_ip.getText().toString();
					
					String ip_home = "192.168.2.2";
					String ip_company = "172.30.67.111";
					
					
					if (ip_address.equals("")) ip_address = ip_company;
					
					String gotPort = et_port.getText().toString();
					if (gotPort.equals("")) gotPort = "55810";
					int port = Integer.parseInt(gotPort);
					
					tv_message.setText("Connecting ... ");
					balance_connector.connect(ip_address, port);
					}
			
			}
        );
        
        bn_manual.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent_manual = new Intent(ConnectionActivity.this, BalanceManual.class);					
				startActivity(intent_manual);
        	}
        }
        );
        
    }

    @Override
    public void onResume()
    {
    	//balance_connector.disconnect();
    	tv_message.setText("");
    	super.onResume();
    	
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basic_weighing, menu);
        return true;
    }
    
}
