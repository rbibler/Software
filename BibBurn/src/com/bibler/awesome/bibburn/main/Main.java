package com.bibler.awesome.bibburn.main;

import java.io.File;

import com.bibler.awesome.bibburn.burnutils.BurnManager;
import com.bibler.awesome.bibburn.serialutils.SerialPortInstance;
import com.bibler.awesome.bibburn.serialutils.SerialPortManager;

public class Main {
	
	public static void main(String[] args) {
		
		SerialPortManager manager = new SerialPortManager();
		SerialPortInstance port = null;
		try {
			port = manager.connect("COM4", 115200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BurnManager burn = new BurnManager(port);
		burn.burnData(new File("C:/users/ryan/desktop/duck_big_prg.bin"));
	}

}
