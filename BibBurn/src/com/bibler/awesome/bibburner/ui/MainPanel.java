package com.bibler.awesome.bibburner.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8304724209111766705L;
	JButton fileButton;
	JButton burnButton;
	JTextField fileField;
	JTextArea messageArea;
	JProgressBar progressBar;
	
	private MainFrame mainFrame;
	private JFileChooser chooser;
	
	public MainPanel(MainFrame mainFrame) {
		super();
		initialize();
		this.mainFrame = mainFrame;
	}
	
	private void initialize() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(450, 350));
		setupButtons(layout);
		setupTextFields(layout);
		setupProgressBar(layout);
		
	}
	
	private void setupButtons(SpringLayout layout) {
		fileButton = new JButton("Load");
		fileButton.setPreferredSize(new Dimension(75, 25));
		burnButton = new JButton("Burn");
		burnButton.setPreferredSize(new Dimension(100, 50));
		layout.putConstraint(SpringLayout.NORTH, fileButton, 50, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, fileButton, 325, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, burnButton, 100, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, burnButton, 175, SpringLayout.WEST, this);
		add(fileButton);
		add(burnButton);
		ButtonListener listener = new ButtonListener();
		fileButton.addActionListener(listener);
		fileButton.setActionCommand("LOAD");
		burnButton.addActionListener(listener);
		burnButton.setActionCommand("BURN");
	}
	
	private void setupTextFields(SpringLayout layout) {
		fileField = new JTextField();
		fileField.setPreferredSize(new Dimension(275, 25));
		messageArea = new JTextArea();
		messageArea.setPreferredSize(new Dimension(400, 100));
		messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		layout.putConstraint(SpringLayout.NORTH, fileField, 50, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, fileField, 0, SpringLayout.WEST, fileButton);
		layout.putConstraint(SpringLayout.NORTH,messageArea, 225, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, messageArea, 25, SpringLayout.WEST, this);
		add(fileField);
		add(messageArea);
		
	}
	
	private void setupProgressBar(SpringLayout layout) {
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(350, 25));
		layout.putConstraint(SpringLayout.NORTH, progressBar, 175, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, progressBar, 50, SpringLayout.WEST, this);
		add(progressBar);
	}
	
	public void updateProgress(float progress) {
		progressBar.setValue((int) (100 * progress));
		repaint();
	}
	
	public void updateMessageArea(String newMessage) {
		String s = messageArea.getText();
		s += (newMessage + '\n');
		messageArea.setText(s);
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = arg0.getActionCommand();
			switch(s) {
			case "BURN":
				mainFrame.startBurn();
				break;
			case "LOAD":
				if(chooser == null) {
					chooser = new JFileChooser("C:/users/ryan/desktop");
				}
				int result = chooser.showOpenDialog(MainPanel.this);
				if(result == JFileChooser.APPROVE_OPTION) {
					mainFrame.loadFile(chooser.getSelectedFile());
				}
				break;
			}
			
		}
		
	}
	
	

}
