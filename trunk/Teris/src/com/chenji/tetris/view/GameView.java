package com.chenji.tetris.view;

import com.chenji.tetris.modle.GameService;
import com.chenji.tetris.modle.Piece;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GameView extends View{
	private GameService gameService;
	

	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void setGameService(GameService gameService)
	{
		this.gameService = gameService;	
		this.gameService.setUIHandler(new Handler(){
			public void handleMessage(Message msg) {
				postInvalidate();
			}
		}
				
		);
	}
	
	

	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if(gameService!=null)
		{
			gameService.initializeBoard(getMeasuredHeight(),getMeasuredWidth());
		}

		this.setMeasuredDimension(getMeasuredWidth()* 8 /10 , getMeasuredHeight());
	}

	@Override
	protected void onDraw(Canvas canvas)
	{		
		if (this.gameService == null)
		{
			return;
		}
		Piece[][] pieces = gameService.getPieces();
		if (pieces != null)
		{
			
			for (int i=0;i<pieces.length;++i)
			{
				for (int j=0;j<pieces[i].length;++j)
				{
					if ((pieces[i][j] != null) && (pieces[i][j].getImage() != null))
					{
						Piece piece = pieces[i][j];
						canvas.drawBitmap(piece.getImage().getImage(), null, piece.getPieceLocation(), null);
					}
				}
			}
		}
		
		super.onDraw(canvas);
		
	}

}
