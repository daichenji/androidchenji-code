package com.chenji.act_levelbubble;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class BubbleView extends View{
	
	private float RELATIVE_VIEW_CENTER_X = 320;
	private float RELATIVE_VIEW_CENTER_Y = 400;
	
	private static int SCALE = 40;
	
	private float OUTER_CYCLE_RADIUM = 6*SCALE; // mm
	private float INTER_CYCLE_RADIUM = 3*SCALE; // mm
	private float BUBBLE_RADIUM = 2*SCALE; // mm
	
	
	private float bubleCenterRadium = 0; // mm
	private float bubbleCenterAngel = 0;

	
	
	public BubbleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setAntiAlias(true);
		
		
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(3);
		
		p.setColor(Color.BLACK);
		
		Path path1 = new Path();
		path1.moveTo((float)RELATIVE_VIEW_CENTER_X - (float)(this.OUTER_CYCLE_RADIUM*1.2), (float)RELATIVE_VIEW_CENTER_Y );
		path1.lineTo((float)RELATIVE_VIEW_CENTER_X + (float)(this.OUTER_CYCLE_RADIUM*1.2), (float)RELATIVE_VIEW_CENTER_Y );
		path1.close();
		canvas.drawPath(path1, p);
		
		Path path2 = new Path();
		path2.moveTo((float)RELATIVE_VIEW_CENTER_X , (float)RELATIVE_VIEW_CENTER_Y - (float)(this.OUTER_CYCLE_RADIUM*1.2));
		path2.lineTo((float)RELATIVE_VIEW_CENTER_X , (float)RELATIVE_VIEW_CENTER_Y + (float)(this.OUTER_CYCLE_RADIUM*1.2));
		path2.close();
		canvas.drawPath(path2, p);
		
		p.setColor(Color.RED);		
		canvas.drawCircle(RELATIVE_VIEW_CENTER_X, RELATIVE_VIEW_CENTER_Y, OUTER_CYCLE_RADIUM, p);
		
		p.setColor(Color.BLUE);
		canvas.drawCircle(RELATIVE_VIEW_CENTER_X, RELATIVE_VIEW_CENTER_Y,INTER_CYCLE_RADIUM , p);
		
		
		p.setStyle(Paint.Style.FILL);
		float radium = bubleCenterRadium*SCALE;
		
		if (radium>(this.OUTER_CYCLE_RADIUM-this.BUBBLE_RADIUM))
		{
			radium = this.OUTER_CYCLE_RADIUM-this.BUBBLE_RADIUM;
			p.setColor(Color.RED);
		}
		else
		{
			p.setColor(Color.BLACK);
		}
		float px = (float) (radium*Math.cos(bubbleCenterAngel));
		float py = (float) (radium*Math.sin(bubbleCenterAngel));
		
		
		
		canvas.drawCircle(RELATIVE_VIEW_CENTER_X+px, RELATIVE_VIEW_CENTER_Y-py, BUBBLE_RADIUM, p);
		
		

		
	}
	
	public void update(double radium, double angel)
	{
		bubleCenterRadium = (float) radium;
		bubbleCenterAngel = (float) angel;
		invalidate();
	}
	

}
