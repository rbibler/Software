package com.bibler.awesome.bibburner.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.bibler.awesome.bibburn.utils.ChipFactory;

public class MainMenu extends JMenuBar {
	
	private JMenu fileMenu;
	private JMenu burnMenu;
	private JMenuItem loadMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu chipsMenuItem;
	private JMenuItem AT28C256MenuItem;
	private JMenuItem AM29F040MenuItem;
	private JMenuItem GLS29EE010MenuItem;
	private MenuListener menuListener;
	
	private MainFrame mainFrame;
	
	public MainMenu(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initializeMenus();
	}
	
	private void initializeMenus() {
		menuListener = new MenuListener();
		initializeFileMenu();
		initializeBurnMenu();
	}
	
	private void initializeFileMenu() {
		fileMenu = new JMenu("File");
		loadMenuItem = new JMenuItem("Open file...");
		loadMenuItem.setActionCommand("OPEN");
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		loadMenuItem.addActionListener(menuListener);
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("EXIT");
		exitMenuItem.addActionListener(menuListener);
		fileMenu.add(loadMenuItem);
		fileMenu.add(exitMenuItem);
		add(fileMenu);
	}
	
	private void initializeBurnMenu() {
		burnMenu = new JMenu("Burner");
		chipsMenuItem = new JMenu("Select Chip...");
		AT28C256MenuItem = new JMenuItem("AT28C256");
		AT28C256MenuItem.setActionCommand("AT28C256");
		AT28C256MenuItem.addActionListener(menuListener);
		GLS29EE010MenuItem = new JMenuItem("GLS29EE010");
		GLS29EE010MenuItem.setActionCommand("GLS29EE010");
		GLS29EE010MenuItem.addActionListener(menuListener);
		AM29F040MenuItem = new JMenuItem("AM29F040");
		AM29F040MenuItem.setActionCommand("AM29F040");
		AM29F040MenuItem.addActionListener(menuListener);
		
		chipsMenuItem.add(AT28C256MenuItem);
		chipsMenuItem.add(GLS29EE010MenuItem);
		chipsMenuItem.add(AM29F040MenuItem);
		burnMenu.add(chipsMenuItem);
		add(burnMenu);
	}
	
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s) {
			case "OPEN":
				mainFrame.openFile();
				break;
			case "EXIT":
				mainFrame.exitProgram();
				break;
			case "AT28C256":
				mainFrame.switchChip(ChipFactory.AT28C256);
				break;
			case "AM29F040":
				mainFrame.switchChip(ChipFactory.AM29F040);
				break;
			case "GLS29EE010":
				mainFrame.switchChip(ChipFactory.GLS29EE010);
				break;
			}
			
		}
		
	}

}
