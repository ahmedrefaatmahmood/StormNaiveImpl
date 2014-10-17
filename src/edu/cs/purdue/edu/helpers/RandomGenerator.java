package edu.cs.purdue.edu.helpers;

import java.util.Random;

public class RandomGenerator extends Random{
	int seed;
	
	public RandomGenerator(int seed) {
		super(seed);
	}
	public double nextDouble(double min,double max){
		return min+(max-min)*nextDouble();
	}

}
