package com.chenji.util;



public class Weight {
	
	private double weightvalue;
	private Unit unit;
	private boolean isStable;
	
	public Weight(double weightvalue, Unit unit, boolean isStable)
	{
		this.weightvalue = weightvalue;
		this.unit = unit;
		this.isStable = isStable;
	}
	
	public double getWeight()
	{
		return weightvalue;
	}
	
	public Unit getUnit()
	{
		return unit;
	}
	
	public double getWeightInGram() // return the weight value in gram
	{
		return unit.convertToGram(weightvalue);
	}
	
	
}
