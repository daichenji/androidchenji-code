package com.chenji.tetris.modle;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chenji.tetris.modle.Shapes.TetrisShape;
import com.chenji.tetris.modle.Shapes.TetrisShapeFactory;

public class GameService {

	private final static int SHAPE_FORCASE_LENGTH = 3;
	
	private TetrisBoard tBoard;
	
	private LinkedList<TetrisShape> currentShapes;
	
	private TetrisShapeFactory tShapeFact;
	
	private Handler UIhandler;
	
	private Timer squareMovingTimer;
	private TimerTask squareMovingTask;
	
	public GameService(TetrisBoard tBoard)
	{
		this.tBoard = tBoard;
		tShapeFact = new TetrisShapeFactory();
		currentShapes = new LinkedList<TetrisShape>();
	}

	public void initializeBoard(int viewHeight, int viewWidth)
	{
		tBoard.initialize(viewHeight,viewWidth);
		startGame();
	}
	
	
	public void startGame()
	{
		
		squareMovingTimer = new Timer();   
		final Handler handler = new Handler(){   
			public void handleMessage(Message msg) { 
				//Log.i("TET","Timer");
				moveDown();
				
				}
		};
		squareMovingTask = new TimerTask(){
			public void run(){
				handler.sendEmptyMessage(0);
			}
		};
		
		shapeForcast();
		tBoard.addShape(popCurrentShape());
		squareMovingTimer.scheduleAtFixedRate(squareMovingTask, 1000,1000);
	}
	
	
	public void setUIHandler(Handler handler)
	{
		UIhandler = handler;
	}
	
	public Piece[][] getPieces()
	{
		return tBoard.getPieces();
	}
	
	public TetrisShape getShape(int index)
	{		
		if (index < SHAPE_FORCASE_LENGTH)
		{
			return currentShapes.get(index);
		}
		else
		{
			return null;
		}
	}
	
	public TetrisShape popCurrentShape()
	{
		TetrisShape currentShape = currentShapes.get(0);
		currentShapes.remove(0);
		currentShapes.add(getRandomShape());
		return currentShape;
	}
	
	public void shapeForcast()
	{
		for(int i=currentShapes.size();i<GameService.SHAPE_FORCASE_LENGTH;++i)
		{

			currentShapes.add(getRandomShape());
		}
	}
	
	private TetrisShape getRandomShape()
	{
		Random random = new Random();
		int type = random.nextInt(tShapeFact.getShapeNumber());	
		TetrisShape shape = tShapeFact.createShape(type,type);	
		int state = random.nextInt(shape.getTotalStateNumber()) ;
		shape.setState(state);
		return shape;
	}
	
	public void OnLeftClick(){
		tBoard.moveLeft();
		updateUI();
	}
	
	public void OnRightClick(){
		tBoard.moveRight();
		updateUI();
	}
	
	public void OnDownClick(){
		moveDown();
	}
	
	public void moveDown()
	{
		if (!tBoard.moveDown())
		{
			touchButton();
		}
		updateUI();
	}
	
	public void OnUpClick(){
//		if (!(tBoard.moveDown() && tBoard.moveDown() && tBoard.moveDown()))
//		{
//			touchButton();
//		}
//		updateUI();
	}
	
	private void touchButton(){
		int fullLineNumber = tBoard.clearFullLine();
		if (!tBoard.checkTopLineEmpty())
		{
			Log.i("TET","END");
			//end game
			squareMovingTimer.cancel();
			
		}
		tBoard.addShape(popCurrentShape());
	}
	
	public void OnToggleClick(){
		tBoard.Toggle();
		updateUI();
	}
	
	public void updateUI(){
		if (UIhandler!=null){
			UIhandler.sendEmptyMessage(0);
		}
	}
	
}
