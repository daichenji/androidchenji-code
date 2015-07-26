package com.chenji.tetris.view;


import java.lang.reflect.Field;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.chenji.tetris.R;

public class PieceImageManager {
	
	public final static int RED = 0;
	public final static int BLUE = 1;
	public final static int BROWN = 2;
	public final static int GREEN = 3;
	public final static int GREY = 4;
	public final static int ORANGE = 5;
	public final static int YELLOW = 6;
	public final static int BLACK = 7;
	
	private final HashMap<String, Integer> nameToIDMap = new HashMap<String, Integer>()
	{
		{  
        put("sq_red", RED);
        put("sq_blue", BLUE);
        put("sq_brown", BROWN);
        put("sq_green", GREEN);
        put("sq_grey", GREY);
        put("sq_orange", ORANGE);
        put("sq_yellow", YELLOW);
        put("sq_black", BLACK);
        }
	};
		

	
	private HashMap<Integer,PieceImage> IDToImageMap = new HashMap<Integer, PieceImage>();
	
	private int viewHeight=0;
	private int viewWidth=0;
	private int BoardHeight = 1;
	private int BoardWidth = 1;
	private int squareHeight = 0;
	private int squareWidth = 0;
	
	
	
	private static PieceImageManager _instance;
	
	public PieceImageManager(Context context)
	{
		
		int resourceValue;
		
		Field[] drawableFields = R.drawable.class.getFields();
		
		
		for (Field field : drawableFields)
		{
			// If the name of the field is expected, save it to the map
			if (nameToIDMap.containsKey(field.getName()))
			{
				
				//Log.i("TETRIS",field.getName());
				
				try {
					resourceValue = field.getInt(R.drawable.class);
					Bitmap bm = BitmapFactory.decodeResource(context.getResources(),  resourceValue);
					// 封装图片ID与图片本身
					PieceImage pieceImage = new PieceImage(bm, resourceValue);
					IDToImageMap.put(nameToIDMap.get(field.getName()), pieceImage);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
					
				
			
		}
		_instance = this;
	}
	
	
	public static PieceImage getSquareImage(int color)
	{
		if (_instance.IDToImageMap.containsKey(color))
		{
			return _instance.IDToImageMap.get(color);
		}
		else
		{
			return null;
		}
		
	}
	

	
	

}
