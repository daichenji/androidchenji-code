package com.chenji.tetris.object;

import android.content.Context;


public class GameConf
{
	// The size of each piece
	public static final int PIECE_WIDTH = 40;
	public static final int PIECE_HEIGHT = 40;
	
	
	// level Speed
	public int levelspeed;;
	
	// level height;
	public int levelheight;
	
	// Piece[][] x
	private int xSize;
	// Piece[][]y
	private int ySize;
	
	// Board�е�һ��ͼƬ���ֵ�x����
	private int beginImageX;
	// Board�е�һ��ͼƬ���ֵ�y����
	private int beginImageY;
	
	private Context context;

	/**
	 * �ṩһ������������
	 * 
	 * @param xSize Piece[][]�����һά����
	 * @param ySize Piece[][]����ڶ�ά����
	 * @param beginImageX Board�е�һ��ͼƬ���ֵ�x����
	 * @param beginImageY Board�е�һ��ͼƬ���ֵ�y����
	 * @param gameTime ����ÿ�ֵ�ʱ��, ��λ����
	 * @param context Ӧ��������
	 */
	public GameConf(int xSize, int ySize, int beginImageX,
		int beginImageY, int speed, int height, Context context)
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.beginImageX = beginImageX;
		this.beginImageY = beginImageY;
		this.levelspeed = speed;
		this.levelheight = height;
		this.context = context;
	}


	public int getXSize()
	{
		return xSize;
	}

	public int getYSize()
	{
		return ySize;
	}

	public int getLevelHeight()
	{
		return levelheight;
	}
	
	public int getLevelSpeed()
	{
		return levelspeed;
	}
	
	public int getBeginImageX()
	{
		return beginImageX;
	}

	public int getBeginImageY()
	{
		return beginImageY;
	}

	public Context getContext()
	{
		return context;
	}
}
