package com.chenji.tetris.modle;

import android.graphics.Rect;

import com.chenji.tetris.view.PieceImage;

public class Piece {
	private PieceImage image;
	private int indexX;
	private int indexY;
	
	private Rect dstLocation;

	
	public Piece(int pieceIndexX, int pieceIndexY)
	{
		setIndexX(pieceIndexX);
		setIndexY(pieceIndexY);
		dstLocation = new Rect();		
	}
	
	public Piece copy()
	{
		Piece p = new Piece(this.getIndexX(),this.getIndexY());
		p.setPieceLocation(this.getPieceLocation());
		p.setImage(this.image);
		return p;
	}
	
	public void setIndexX(int indexX)
	{
		this.indexX = indexX;
	}	
	
	public int getIndexX()
	{
		return this.indexX;
	}
	
	
	
	public void setIndexY(int indexY)
	{
		this.indexY = indexY;
	}	

	public int getIndexY()
	{
		return this.indexY;
	}
	

	
	public void setPieceLocation(Rect dst)
	{
		this.dstLocation = dst;
	}
	
	public Rect getPieceLocation()
	{
		return this.dstLocation;
	}
	
	public void setImage(PieceImage image)
	{
		this.image = image;
	}
	
	public void removeImage()
	{
		this.image = null;
	}
	
	public PieceImage getImage()
	{
		return image;
	}
}

