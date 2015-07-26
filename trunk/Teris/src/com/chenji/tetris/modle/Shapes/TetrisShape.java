package com.chenji.tetris.modle.Shapes;

public abstract class TetrisShape {

	protected int indexX;
	protected int indexY;
	protected int ShapeState;
	protected int color;
	
	protected int boardWidth;
	protected int boardHeight;
	
	
	protected TetrisShape(int color)
	{
		
		this.color = color;
		
		
		ShapeState = 0;
		indexX = 0;
		indexY = 0;
		
	}
	
	abstract protected int[][][] getShapeStates(); 
	
	public final void setX(int x)
	{
		this.indexX = x;
	}
	
	public final int getX()
	{
		return this.indexX;
	}

	public final void setY(int y)
	{
		this.indexY = y;
	}
	
	public final int getY()
	{
		return this.indexY;
	}
	
	public final void setState(int state)
	{
		ShapeState = state % getTotalStateNumber();
	}
	
	public final int getState()
	{
		return ShapeState;
	}

	public final int[][] getShapePieces(int state)
	{
		return this.getShapeStates()[state];
	}
	
	public final int[][] getCurrentPieces()
	{
		return getShapePieces(ShapeState);
	}
	
	
	public final int getNextState()
	{
		return (ShapeState +1 ) % getTotalStateNumber();
	}
	
	public final int getTotalStateNumber()
	{
		return getShapeStates().length;
	}
	
	public final int getXLeft()
	{
		return this.indexX - 1;
	}
	
	public final int getXRight()
	{
		return this.indexX + 1;
	}
	
	public final int getYDown()
	{
		return this.indexY + 1;
	}
	
	public final int getColor()
	{
		return color;
	}
	
	public final int getWidth()
	{
		int left;
		
		int[][] pieces = getShapePieces(ShapeState);
		int currentValue=0;
		int maxValue = 0;
		for(int i=0;i<pieces.length;++i)
		{
			left = -1;
			for(int j=0;j<pieces[i].length;++j)
			{
				int piece = pieces[i][j];
				if(piece>0)
				{
					if(left<0)
					{
						left = j;
					}
					currentValue = j-left+1;
				}
				
			}
			if (maxValue<currentValue)
			{
				maxValue = currentValue;
			}				
		}
		return maxValue;
		
	}
	
	
	public final int getHeight()
	{
		int top;
		
		int[][] pieces = getShapePieces(ShapeState);
		int currentValue=0;
		int maxValue = 0;
		for(int j=0;j<pieces[0].length;++j)
		{
			top = -1;
			for(int i=0;i<pieces.length;++i)
			{
				int piece = pieces[i][j];
				if(piece>0)
				{
					if(top<0)
					{
						top = j;
					}
					currentValue = j-top+1;
				}
				
			}
			if (maxValue<currentValue)
			{
				maxValue = currentValue;
			}				
		}
		return maxValue;
		
	}
}
