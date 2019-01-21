package com.Amarasekera.genetic;

import java.util.Arrays;
import java.util.Random;

public class Individual {

	private int[] genes;
	private int fitness;
	private Random randomGenerator;
	
	public Individual(){
		this.genes = new int[Constants.CHROMOSOME_LENGTH];
		this.randomGenerator = new Random();
	}
	
	public void generateIndividual(){
		for(int i=0;i<Constants.CHROMOSOME_LENGTH;i++){
			int gene = randomGenerator.nextInt(2);
			genes[i] = gene;
		}
	}
	
	public double f(double x){
		return Math.sin(x)*((x-2)*(x-2))+3;
	}
	
	public double getFitness(){
		double genesToDouble = genesToDoubles();
		return f(genesToDouble);
	}

	public double genesToDoubles(){
		
		int base = 1;
		double geneInDouble = 0;
		
		for(int i = 0;i<Constants.GENE_LENGTH;i++){
			if(this.genes[i]==1){
				geneInDouble += base;
			}
			
			base = base*2;
		}
		geneInDouble = geneInDouble/102.4f;
		
		return geneInDouble;
	}
	
	public int getGene(int index) {
		return this.genes[index];
	}
	
	public void setGene(int index, int value){
		this.genes[index] = value;
		this.fitness = 0;
	}

	@Override
	public String toString() {
		String s = "";
		
		for(int i=0;i<Constants.CHROMOSOME_LENGTH;i++){
			s+= getGene(i);
		}
		
		return s;
	}
	
}
