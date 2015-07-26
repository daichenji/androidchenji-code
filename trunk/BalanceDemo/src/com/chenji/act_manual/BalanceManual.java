package com.chenji.act_manual;

import com.chenji.balance.R;


import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BalanceManual extends Activity {
	
	
	
	
	Button bn_open;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balance_manual);
		bn_open = (Button)findViewById(R.id.manual_1_bn);
		
		bn_open.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BalanceManual.this, DownloadManual.class);
				startActivity(intent);
			}
			}
		);
		
		bn_open.setText(getFilesDir().getAbsolutePath());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.balance_manual, menu);
		return true;
	}

}
