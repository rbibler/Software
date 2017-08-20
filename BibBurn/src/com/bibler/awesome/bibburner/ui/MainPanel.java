package com.bibler.awesome.bibburner.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

import com.bibler.awesome.bibburn.utils.TimeUtils;

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
	JScrollPane messageScrollPane;
	JLabel timeLabel;
	
	private MainFrame mainFrame;
	private JFileChooser chooser;
	
	public MainPanel(MainFrame mainFrame) {
		super();
		initialize();
		this.mainFrame = mainFrame;
		setBorder(BorderFactory.createLineBorder(Color.RED, 6));
	}
	
	private void initialize() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		setPreferredSize(new Dimension(450, 350));
		setupProgressBar(layout);
		setupButtons(layout);
		setupTextFields(layout);
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
		messageArea.setMinimumSize(new Dimension(400, 100));
		messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		messageArea.setLineWrap(true);
		messageScrollPane = new JScrollPane(messageArea);
		messageScrollPane.setPreferredSize(new Dimension(400, 100));
		messageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		timeLabel = new JLabel("--:--/--:--");
		layout.putConstraint(SpringLayout.NORTH, fileField, 50, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, fileField, 0, SpringLayout.WEST, fileButton);
		layout.putConstraint(SpringLayout.NORTH,messageScrollPane, 225, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, messageScrollPane, 25, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, timeLabel, 0, SpringLayout.EAST, progressBar);
		layout.putConstraint(SpringLayout.NORTH, timeLabel, 5, SpringLayout.SOUTH, progressBar);
		add(fileField);
		add(messageScrollPane);
		add(timeLabel);
		
	}
	
	private void setupProgressBar(SpringLayout layout) {
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(350, 25));
		layout.putConstraint(SpringLayout.NORTH, progressBar, 175, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, progressBar, 50, SpringLayout.WEST, this);
		add(progressBar);
	}
	
	public void updateProgress(float progress, long timeElapsed, long timeRemaining) {
		progressBar.setValue((int) (100 * progress));
		timeLabel.setText("Elapsed " + TimeUtils.millisToMinutes(timeElapsed, false) + " Remaining: " + TimeUtils.millisToMinutes(timeRemaining, false));
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
				fileField.setText(mainFrame.openFile());
				break;
			}
			
		}
		
	}

	public void updateProgressBarColor(Color color) {
		setBorder(BorderFactory.createLineBorder(color));
		repaint();
		
	}
}
