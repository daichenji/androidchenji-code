package com.chenji.tetris.modle;


import java.util.Random;


import android.graphics.Rect;
import android.util.Log;

import com.chenji.tetris.modle.Shapes.TetrisShape;
import com.chenji.tetris.object.GameConf;
import com.chenji.tetris.view.PieceImageManager;

public class TetrisBoard {
	private Piece[][] pieces;
	
	private TetrisShape currentShape;
	private GameConf gameConfig;
	
	public TetrisBoard(GameConf gameConfig)
	{
		this.gameConfig = gameConfig;
	}
	
	public void initialize(int viewHeight, int viewWidth)
	{
		
		int imageHeight = viewHeight/gameConfig.getYSize();
		//int imageWidth = viewWidth/gameConfig.getXSize();
		int imageWidth = imageHeight;
		
		int beginImageX = gameConfig.getBeginImageX();
		int beginImageY = gameConfig.getBeginImageY();

		
		pieces = new Piece[gameConfig.getYSize()][gameConfig.getXSize()];
		
		for (int i=0; i< pieces.length ; ++i)
		{
			
			
			for (int j=0; j<pieces[i].length; ++j)
			{
				Piece piece = new Piece(j , i);
				
				Rect dstLocation = new Rect();
				
				dstLocation.left = beginImageX + piece.getIndexX() * imageWidth;
				dstLocation.right = dstLocation.left + imageWidth;
				
				dstLocation.top = beginImageY + piece.getIndexY() * imageHeight;
				dstLocation.bottom = dstLocation.top + imageHeight;
				
				piece.setPieceLocation(dstLocation);
				
				pieces[i][j] = piece;
				
			}
			
		}
		

	}
	
	
	public void addShape(TetrisShape shape)
	{
		currentShape = shape;
		int height = shape.getHeight();
		int width = shape.getWidth();

		int x = (gameConfig.getXSize()-width)/2;
		int y = 0;
		currentShape.setX(x);
		currentShape.setY(y);		
	}
	
	public boolean moveLeft()
	{
		int valueX = currentShape.getXLeft();
		if (checkHit(valueX,currentShape.getY(),currentShape.getState()))
		{
			currentShape.setX(valueX);
			return true;
		}
		return false;
	}

	public boolean moveRight()
	{
		int valueX = currentShape.getXRight();
		if (checkHit(valueX,currentShape.getY(),currentShape.getState()))
		{
			currentShape.setX(valueX);
			return true;
		}
		return false;
	}
	
	public boolean moveDown()
	{
		int valueY = currentShape.getYDown();
		if (checkHit(currentShape.getX(),valueY,currentShape.getState()))
		{
			currentShape.setY(valueY);
			return true;
		}
		
		
		fixShapeToBoard();
		
		return false;
	}
	
	public boolean Toggle()
	{
		int state = currentShape.getNextState();
		if (checkHit(currentShape.getX(),currentShape.getY(),state))
		{
			currentShape.setState(state);
			return true;
		}

		return false;
	}
	
	public void fixShapeToBoard()
	{
		// fix the shape to the board		
		int[][] shapePieces = currentShape.getCurrentPieces();
		int shapeX = currentShape.getX();
		int shapeY = currentShape.getY();
		int shapeColor = currentShape.getColor();
		
		for(int i=0;i<shapePieces.length;++i)
		{
			for(int j=0;j<shapePieces[i].length;++j)
			{
				if (shapePieces[i][j]>0)
				{					
					pieces[shapeY+i][shapeX+j].setImage(PieceImageManager.getSquareImage(shapeColor));
				}
			}
		}

	}
	
	public int clearFullLine()
	{
		boolean isFull;
		int numberOfFullLine = 0;
		//String result="====================\n";
		
		for(int i=gameConfig.getYSize()-1;i>=0;)
		{
			isFull = true;
			//String lineStr = "";
			
			for(int j=0;j<gameConfig.getXSize();++j)
			{
				
				if (pieces[i][j].getImage() == null)
				{
					isFull=false;
					//lineStr += 0;
					
					break;
				}
				else
				{
					//lineStr += 1;
				}
			}
			//result = lineStr+"\n"+result;
			
			if (isFull == false)
			{
				--i;
			}
			else
			{
				numberOfFullLine += 1;
				
				for(int k=i;k>0;--k)
				{
					for(int j=0;j<gameConfig.getXSize();++j)
					{
						pieces[k][j].setImage(pieces[k-1][j].getImage());
					}
				}
				for(int j=0;j<gameConfig.getXSize();++j)
				{
					pieces[0][j].removeImage();
				}
			}
		}
		
		
		//Log.i("TET",result);
		return numberOfFullLine;
	}
	
	public boolean checkTopLineEmpty()
	{
		// The top line should have not image
		for(int j=0;j<gameConfig.getXSize();++j)
		{
			if (pieces[0][j].getImage() != null){
				return false;
			}
		}
		return true;
	}
	
	public boolean checkHit(int shapeX, int shapeY, int shapeState) // Check whether the current shape will hit the existing board
	{
		int[][] shapePieces = currentShape.getShapePieces(shapeState);
		int boardWidth = gameConfig.getXSize();
		int boardHeight = gameConfig.getYSize();
		
		for(int i=0;i<shapePieces.length;++i)
		{
			for(int j=0;j<shapePieces[i].length;++j)
			{
				if (shapePieces[i][j]>0)
				{
					if ((shapeX+j<0) 
						|| (shapeX+j>=boardWidth)
						|| (shapeY+i<0)
						|| (shapeY+i>=boardHeight))
					{
						return false;
					}
					
					if(pieces[shapeY+i][shapeX+j].getImage()!=null)
					{
						return false;
					
					}
				}
			}
		}
		return true;
	}

	
	public Piece[][] getPieces()
	{		
		Piece[][] pieceresult = new Piece[gameConfig.getYSize()][gameConfig.getXSize()];
		for (int i=0; i< pieces.length ; ++i)
		{		
			for (int j=0; j<pieces[i].length; ++j)
			{
				pieceresult[i][j] = pieces[i][j].copy();
					
			}
		}
		
		if (currentShape!=null)
		{
			int locX = currentShape.getX();
			int locY = currentShape.getY();
			
			
			int color = currentShape.getColor();
			int[][] shapePieces = currentShape.getCurrentPieces();
			
			for(int i=0;i<shapePieces.length;++i)
			{
				for(int j=0;j<shapePieces[i].length;++j)
				{
					if (shapePieces[i][j]>0)
					{						
						pieceresult[i+locY][j+locX].setImage(PieceImageManager.getSquareImage(color));
				
					}
				}
			}
		}
		
		return pieceresult;
	}
	

}
