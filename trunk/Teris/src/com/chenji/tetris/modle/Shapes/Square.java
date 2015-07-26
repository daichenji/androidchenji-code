package com.chenji.tetris.modle.Shapes;

import java.util.Random;

public class Square extends TetrisShape{
	
	
	protected Square(int color, int boardWidth, int boardHeight) {
		super(color, boardWidth, boardHeight);
		// TODO Auto-generated constructor stub
	}

	private int[][][] shapeStates = 
		{
			{
				{1,1}, 
				{1,1}
			}
		};

	public void create(){
		Random random = new Random();
		random.nextInt();
		
		shapeStates = new int[3][][];
		
		
	}
	
	
	@Override
	protected void initialShapeStates() {
		// TODO Auto-generated method stub
		
	}
	


}
