package com.bibler.awesome.bibburner.ui;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bibler.awesome.bibburn.burnutils.BurnManager;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 278240709235417078L;
	
	private MainPanel mainPanel;
	private BurnManager burner;
	private MainMenu mainMenu;
	private JFileChooser chooser;
	
	public MainFrame(BurnManager burner) {
		super();
		this.burner = burner;
		burner.setMainFrame(this);
		
		try {
	        UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
			} 
			catch (UnsupportedLookAndFeelException | ClassNotFoundException  | InstantiationException | IllegalAccessException e) {}
		mainPanel = new MainPanel(this);
		initializeMenus();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPanel);
		pack();
		setVisible(true);
	}
	
	public void updateBurnProgress(float progress, long timeElapsed, long totalTime) {
		mainPanel.updateProgress(progress, timeElapsed, totalTime);
	}
	
	public void updateProgressBarColor(Color color) {
		mainPanel.updateProgressBarColor(color);
	}
	
	public void exitProgram() {
		this.dispose();
	}
	
	public void switchChip(int chipToSwitch) {
		burner.setNewChip(chipToSwitch);
	}
	
	public String openFile() {
		if(chooser == null) {
			chooser = new JFileChooser("C:/users/ryan/desktop");
		}
		int result = chooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			burner.loadFile(chooser.getSelectedFile());
		}
		return chooser.getSelectedFile().getAbsolutePath();
	}
	
	public void startBurn() {
		burner.sendBurnCommand();
	}
	
	public void updateMessageArea(String newMessage) {
		mainPanel.updateMessageArea(newMessage);
	}
	
	private void initializeMenus() {
		mainMenu = new MainMenu(this);
		setJMenuBar(mainMenu);
	}
	

}
