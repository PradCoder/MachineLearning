package com.conwaygame.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel timeLabel;
	public int count = 0;
	
	public TimePanel(){
		initializeLayout();
	}
	
	private void initializeLayout(){
		setLayout(new FlowLayout (FlowLayout.CENTER));
		timeLabel = new JLabel("Generations: 0");
		add(timeLabel);
	}
	
	public void refreshCounter(){
		count = 0;
		refresh();
	}
	
	public void refresh(){
		timeLabel.setText("Generations: "+(count++));
	}
}
