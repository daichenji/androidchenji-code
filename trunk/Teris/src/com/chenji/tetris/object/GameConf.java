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
	
	// Board中第一张图片出现的x座标
	private int beginImageX;
	// Board中第一张图片出现的y座标
	private int beginImageY;
	
	private Context context;

	/**
	 * 提供一个参数构造器
	 * 
	 * @param xSize Piece[][]数组第一维长度
	 * @param ySize Piece[][]数组第二维长度
	 * @param beginImageX Board中第一张图片出现的x座标
	 * @param beginImageY Board中第一张图片出现的y座标
	 * @param gameTime 设置每局的时间, 单位是秒
	 * @param context 应用上下文
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
