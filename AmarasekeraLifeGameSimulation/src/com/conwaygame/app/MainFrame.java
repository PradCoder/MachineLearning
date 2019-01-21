package com.conwaygame.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.conwaygame.callbacks.ButtonListener;
import com.conwaygame.constants.Constants;
import com.conwaygame.gui.Board;
import com.conwaygame.gui.Controller;
import com.conwaygame.gui.TimePanel;
import com.conwaygame.gui.Toolbar;

public class MainFrame extends JFrame implements ButtonListener{

	private static final long serialVersionUID = 1L;
	private Board board;
	private Toolbar toolbar;
	private TimePanel timePanel;
	private ExecutorService executor;
	
	public MainFrame(){
		super(Constants.NAME_OF_APPLICATION);
		initializeMenu();
		initializeMainLayout();
	}
	
	private void initializeMainLayout(){
		toolbar = new Toolbar();
		timePanel = new TimePanel();
		board = new Board(timePanel);
		
		toolbar.setButtonListener(this);
		
		add(board,BorderLayout.CENTER);
		add(toolbar,BorderLayout.NORTH);
		add(timePanel,BorderLayout.SOUTH);
		
		setSize(Constants.FRAME_WIDTH,Constants.FRAME_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void initializeMenu(){
		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu(Constants.MENU_FILE);
		JMenuItem exitMenuItem = new JMenuItem(Constants.MENU_EXIT);
		fileMenu.add(exitMenuItem);
		
		JMenu aboutMenu = new JMenu(Constants.MENU_ABOUT);
		
		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);
		
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				int action = JOptionPane.showConfirmDialog(MainFrame.this, Constants.MENU_EXIT_TEXT,Constants.MENU_EXIT_TITLE,JOptionPane.YES_NO_OPTION);
				if(action == JOptionPane.OK_OPTION){
					System.gc();
					System.exit(0);
				}
			}
		});
		
		return menuBar;
	}
	
	@Override
	public void startClicked(){	
		
		SwingUtilities.invokeLater(new Runnable (){
			public void run(){
				toolbar.setStartButton(false);
				toolbar.setRestartButton(true);
			}
		});
		
		Controller.startThread();
		executor = Executors.newSingleThreadExecutor();
		executor.execute(new Controller(board));
	}
	
	@Override
	public void restartClicked(){
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				toolbar.setStartButton(true);
				toolbar.setRestartButton(false);
			}
		});
		
		executor.shutdown();
		Controller.stopThread();
		board.resetBoard();
		timePanel.refreshCounter();
		
		toolbar.setRestartButton(false);
	}
}
