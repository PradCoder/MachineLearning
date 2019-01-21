package com.conwaygame.app;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

	public static void main(String[] args) throws IOException, URISyntaxException{
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(ClassNotFoundException|InstantiationException
				|IllegalAccessException|UnsupportedLookAndFeelException e){
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MainFrame();
			}
		});
	}

}
