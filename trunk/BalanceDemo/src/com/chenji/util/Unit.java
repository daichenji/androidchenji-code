package com.chenji.util;

import java.util.ArrayList;

public class Unit {
	
	private static ArrayList<Unit> UnitList = new ArrayList<Unit>();
	
	public final static Unit gram = new Unit("g",1);
	public final static Unit miligram = new Unit("mg",0.001);
	public final static Unit kilogram = new Unit("kg",1000);
	public final static Unit ct = new Unit("ct",0.2);
	
	public static Unit getUnitByName(String name)
	{
		for(int i=0;i<UnitList.size();++i)
		{
			if (UnitList.get(i).matchName(name))
			{
				return UnitList.get(i);
				}		  
			}
		return null;
	}
	
	
	
	private String unittext = null;
	private double unitrate = 1; // comparing to gram, e.g 0.001 for mg
	
	
	
	public Unit(String text, double rate)
	{
		unittext = text;
		unitrate = rate;
		Unit.UnitList.add(this);
	}
	
	public double convertToGram(double weight)
	{
		return weight*unitrate;
	}
	
	public double convertFromGram(double weight)
	{
		return weight/unitrate;
	}
	
	public String getName()
	{
		return unittext;
	}
	
	public boolean matchName(String name)
	{
		return unittext.equals(name);
	}
}
