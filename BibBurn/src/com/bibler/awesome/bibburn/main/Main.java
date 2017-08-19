package com.bibler.awesome.bibburn.main;

import com.bibler.awesome.bibburn.burnutils.BurnManager;
import com.bibler.awesome.bibburn.serialutils.SerialPortInstance;
import com.bibler.awesome.bibburn.serialutils.SerialPortManager;
import com.bibler.awesome.bibburner.ui.MainFrame;

public class Main {
	
	public static void main(String[] args) {
		
		SerialPortManager manager = new SerialPortManager();
		SerialPortInstance port = null;
		try {
			port = manager.connect("COM4", 115200);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BurnManager burn = new BurnManager(port);
		MainFrame mainFrame = new MainFrame(burn);
	}

}
