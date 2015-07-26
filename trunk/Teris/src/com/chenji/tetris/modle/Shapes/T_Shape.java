package com.chenji.tetris.modle.Shapes;

import java.util.Random;

public class T_Shape extends TetrisShape{
	
	
	protected T_Shape(int color, int boardWidth, int boardHeight) {
		super(color, boardWidth, boardHeight);
		// TODO Auto-generated constructor stub
	}

	private final static int[][][] shapeStates = 
		{
			{
				{0,1,0},
				{1,1,1},
				{0,0,0}
			},
			{
				{0,1,0},
				{0,1,1},
				{0,1,0}
			},
			{
				{0,0,0},
				{1,1,1},
				{0,1,0}
			},
			{
				{0,1,0},
				{1,1,0},
				{0,1,0}
			}
		};
	
	private int currentType = 0;
	
	public void create(){
		currentType = (new Random().nextInt()) % shapeStates.length;		
		
	}

	@Override
	protected void initialShapeStates() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
