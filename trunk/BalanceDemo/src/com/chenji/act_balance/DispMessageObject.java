package com.chenji.act_balance;


import java.util.HashMap;
import java.util.LinkedHashMap;



public class DispMessageObject {
	public static final int MAIN = 0x01;
	public static final int ICONS = 0x02;
	
	HashMap<Integer,String> display;
	
	public DispMessageObject(){
		display = new LinkedHashMap<Integer,String>();
	}
	
	public DispMessageObject(int type,String content){
		this();
		addContent(type,content);
	}
	
	public void addContent(int type,String content)
	{
		display.put(type, content);
	}
	
	public String getContent(int type)
	{
		if (display.containsKey(type))
		{
			return display.get(type);
		}
		else{
			return null;
		}
		
	}
	

}
