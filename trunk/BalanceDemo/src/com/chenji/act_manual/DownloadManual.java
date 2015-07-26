package com.chenji.act_manual;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chenji.balance.R;

public class DownloadManual extends Activity
{
	
	String urlstr = "http://cn.mt.com/cn/zh/home/supportive_content/product_documentation/product_brochures/NewClassic_MS_GB_HR/_jcr_content/download/file/file.res/0905_NewClassic_MS_12310384.pdf";
	
	
	
	EditText url;
	EditText target;
	Button downBn;
	ProgressBar bar;
	DownUtil downUtil;
	Button viewBn;
	
	String downloadFile = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_manual);

		url = (EditText) findViewById(R.id.url);
		url.setText(urlstr);
		
		target = (EditText) findViewById(R.id.target);
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			String root = Environment.getExternalStorageDirectory().getAbsolutePath();
			File file = new File(root+"/balancemanual");
			file.mkdir();
			target.setText(root+"/first.pdf");
		}
		else
		{
			target.setText("None");
		}
		
		downBn = (Button) findViewById(R.id.down);
		bar = (ProgressBar) findViewById(R.id.bar);
		final Toast downsuccessToast = Toast.makeText(this, "Download Successfully!", Toast.LENGTH_LONG);
		// 创建一个Handler对象
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				if (msg.what == 0x123)
				{
					bar.setProgress((Integer) msg.obj);
					if ((Integer) msg.obj >= 100)
					{
						downsuccessToast.show();
				
					}
				}
				
			}
		};
		downBn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				downloadFile = target.getText().toString();
				// 初始化DownUtil对象
				downUtil = new DownUtil(url.getText().toString(),
						downloadFile, 8, handler);
				
				
				new Thread(downUtil).start();
				
				
			}
		});
		
		viewBn = (Button)findViewById(R.id.view);
		final Builder noDownloadDialog = new AlertDialog.Builder(this);
		viewBn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				if (downloadFile == null)
				{
					noDownloadDialog.setTitle("No Download").show();
				}
				else
				{
					File file = new File(downloadFile);
					Uri path = Uri.fromFile(file);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(path, "application/pdf");
					try {
					     startActivity(intent);
					}  
					     catch (ActivityNotFoundException e) {
					     e.printStackTrace();
					} 
				}
				
			}
			
		});
	}
}