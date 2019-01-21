package com.Amarasekera.hopfield;

public class Utils {

	public static double[] transform(double[] pattern){
		
		for(int i=0;i<pattern.length;++i){
			if(pattern[i]==0){
				pattern[i] =-1;
			}	
		}
		return pattern;
	}
}
