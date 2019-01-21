package com.conwaygame.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import com.conwaygame.constants.Constants;

public class Board extends JPanel{

	private static final long serialVersionUID = 1L;
	private Cell[] cells;
	private Set<Integer> cellsToDie = new HashSet<>();
	private Set<Integer> cellsToBorn = new HashSet<>();
	private int countLiveNeighbours = 0;
	private TimePanel timePanel;
	
	public Board(TimePanel timePanel){
		this.timePanel = timePanel;
		
		initializeLayout();
		paintBoard();
	}
	
	private void initializeLayout(){
		cells = new Cell[Constants.NUMBER_OF_ROWS*Constants.NUMBER_OF_COLUMNS];
		GridLayout gridLayout = new GridLayout(Constants.NUMBER_OF_ROWS,Constants.NUMBER_OF_COLUMNS);
		setLayout(gridLayout);
	}
	
	public void refreshBoard(int id){
		if(cells[id].isAlive()){
			cells[id].setBackground(Color.decode(Constants.GREEN_COLOR));
		}else{
			cells[id].setBackground(Color.WHITE);
		}
	}
	
	public void newIteration(){
		for(int i=0;i<Constants.NUMBER_OF_ROWS*Constants.NUMBER_OF_COLUMNS;i++){
			
			countLiveNeighbours=0;
			
			if(i<=Constants.NUMBER_OF_ROWS-1||i>((Constants.NUMBER_OF_ROWS-1)*Constants.NUMBER_OF_COLUMNS-1)|| (i%Constants.NUMBER_OF_ROWS)-(Constants.NUMBER_OF_ROWS-1)==0 ||i%Constants.NUMBER_OF_ROWS==0){
				continue;
			}
			if(cells[i-1].isAlive()){countLiveNeighbours++;}
			if(cells[i+1].isAlive()){countLiveNeighbours++;}
			if(cells[i-Constants.NUMBER_OF_ROWS+1].isAlive()){countLiveNeighbours++;}
			if(cells[i-Constants.NUMBER_OF_ROWS-1].isAlive()){countLiveNeighbours++;}
			if(cells[i-Constants.NUMBER_OF_ROWS].isAlive()){countLiveNeighbours++;}
			if(cells[i+Constants.NUMBER_OF_ROWS+1].isAlive()){countLiveNeighbours++;}
			if(cells[i+Constants.NUMBER_OF_ROWS].isAlive()){countLiveNeighbours++;}
			if(cells[i+Constants.NUMBER_OF_ROWS-1].isAlive()){countLiveNeighbours++;}
		
			//cell with exactly 3 neighbours --> becomes a live cell
			if(countLiveNeighbours==3 && !cells[i].isAlive()){
				cellsToBorn.add(i);
			}
			
			//underpopulation and overpopulation
			if(countLiveNeighbours<2||countLiveNeighbours>3){
				cellsToDie.add(i);
			}
			
			//any live cell with 2 or 3 neighbours on to the next generation
			if(countLiveNeighbours==3||countLiveNeighbours==2&&cells[i].isAlive()){
				cellsToBorn.add(i);
			}
		}
		
		repaintBoard();
		timePanel.refresh();
	}
	
	public void repaintBoard(){
		
		for(Integer integer : cellsToBorn){
			cells[integer].setBackground(Color.decode(Constants.GREEN_COLOR));
			cells[integer].setAlive(true);
		}
		
		for(Integer integer : cellsToDie){
			cells[integer].setBackground(Color.WHITE);
			cells[integer].setAlive(false);
		}
		
		cellsToBorn.clear();
		cellsToDie.clear();
	}
	
	public void paintBoard(){
		for(int i=0;i<Constants.NUMBER_OF_ROWS*Constants.NUMBER_OF_COLUMNS;i++){
			cells[i] = new Cell(i,this);
			cells[i].setAlive(false);
			add(cells[i]);
		}
	}
	
	public void resetBoard(){
		for(int i=0;i<Constants.NUMBER_OF_ROWS*Constants.NUMBER_OF_COLUMNS;i++){
			cells[i].setAlive(false);
			cells[i].setBackground(Color.WHITE);
		}
	}
}
