package com.bibler.awesome.bibburner.ui;

import java.io.File;

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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(mainPanel);
		pack();
		setVisible(true);
	}
	
	public void updateBurnProgress(float progress) {
		mainPanel.updateProgress(progress);
	}
	
	public void loadFile(File f) {
		burner.loadFile(f);
	}
	
	public void startBurn() {
		burner.sendBurnCommand();
	}
	
	public void updateMessageArea(String newMessage) {
		mainPanel.updateMessageArea(newMessage);
	}
	

}
