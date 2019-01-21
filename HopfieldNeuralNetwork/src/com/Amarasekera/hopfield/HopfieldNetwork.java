package com.Amarasekera.hopfield;

public class HopfieldNetwork {

	private double[][] weighMatrix;
	
	public	HopfieldNetwork(int dimension){
		this.weighMatrix = new double[dimension][dimension];
	}
	
	public void train(double[] pattern){
		
		double[] patternBipolar = Utils.transform(pattern);
		double[][] patternMatrix = Matrix.createMatrix(patternBipolar.length, patternBipolar.length);
	
		patternMatrix = Matrix.outerProduct(patternBipolar);
		patternMatrix = Matrix.clearDiagonals(patternMatrix);
		
		this.weighMatrix = Matrix.addMatrix(weighMatrix, patternMatrix);
	}
	
	public void recall(double[] pattern){
		
		double[] patternBipolar = Utils.transform(pattern);
		
		double[] result = Matrix.matrixVectorMultiplication(this.weighMatrix, patternBipolar);
		
		for(int i=0;i<patternBipolar.length;++i){
			result[i] = ActivationFunction.stepFunction(result[i]);
		}
		
		for(int i=0;i<patternBipolar.length;++i){
			if(patternBipolar[i] != result[i]){
				System.out.println("Pattern is not recognized...");
				return;
			}
		}
		System.out.println("Pattern is recognized");
	}
}
