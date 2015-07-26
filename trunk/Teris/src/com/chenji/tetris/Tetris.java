package com.chenji.tetris;

import com.chenji.tetris.modle.GameService;
import com.chenji.tetris.modle.TetrisBoard;
import com.chenji.tetris.object.GameConf;

import com.chenji.tetris.view.GameView;
import com.chenji.tetris.view.PieceImageManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Tetris extends Activity {
	
	private GameConf gameconfig;
	GameService gameService;
	
	private Button bn_Up;
	private Button bn_Down;
	private Button bn_Left;
	private Button bn_Right;
	private Button bn_Toggle;
	private Button bn_Function2;
	private GameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		
		
		bn_Up = (Button)findViewById(R.id.up);
		bn_Down = (Button)findViewById(R.id.down);
		bn_Left = (Button)findViewById(R.id.left);
		bn_Right = (Button)findViewById(R.id.right);
		
		bn_Toggle = (Button)findViewById(R.id.function1);
		bn_Function2 = (Button)findViewById(R.id.function2);
		
		gameView = (GameView)findViewById(R.id.gameView);
		
		new PieceImageManager(this);
		
		
		int xSize = 12;
		int ySize = 18;
		
		gameconfig = new GameConf(xSize, ySize, 0, 0, 1, ySize, this);
		
		
		TetrisBoard tetirsBoard = new TetrisBoard(gameconfig);
		
		gameService = new GameService(tetirsBoard);
		
		gameView.setGameService(gameService);
		
//		DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;     // ÆÁÄ»¿í¶È£¨ÏñËØ£©
//        int height = metric.heightPixels;   // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
//        
//		gameService.initializeBoard(width*7/8,height);
		
		
		bn_Left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnLeftClick();				
			}
		});
		
		
		bn_Left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnLeftClick();				
			}
		});
		
		bn_Right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnRightClick();				
			}
		});
		
		bn_Down.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnDownClick();				
			}
		});
		
		bn_Up.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnUpClick();				
			}
		});
		
		bn_Toggle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				gameService.OnToggleClick();				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
