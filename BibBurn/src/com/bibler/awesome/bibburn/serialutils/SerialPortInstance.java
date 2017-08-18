package com.bibler.awesome.bibburn.serialutils;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.bibler.awesome.bibburn.burnutils.BurnManager;

/**
 *
 * @author mario
 */
public class SerialPortInstance implements Runnable {

	private InputStream in = null;
	private OutputStream out = null;
	private SerialPort serialPort = null;
	private BurnManager burnManager;

	public SerialPortInstance(SerialPort serialPort) {
		in = null;
	    out = null;
	    this.serialPort = serialPort;
	    setupStreamsAndBuffers();
	    Thread t = new Thread(this);
	    t.start();
	}
	
	public void setBurnManager(BurnManager burnManager) {
		this.burnManager = burnManager;
	}
	
	private void setupStreamsAndBuffers() {
		try {
			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	    
	public InputStream getSerialIn() {
	    return in;
	}
	    
	public OutputStream getSerialOut() {
		return out;
	}
	
	private void handleSerialInput() {
		int read;
		try {
			do {
				read = in.read();
				burnManager.receiveSerialData(read);
			} while(read != -1 && in.available() > 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				if(in.available() > 0) {	
					handleSerialInput();
				}
			} catch (IOException e) {}
		}	
	}

	public void write(int byteToWrite) {
		try {
			out.write(byteToWrite);
			out.flush();
		} catch(IOException e) {}
	}
	
	public void writeBlock(int[] block, int startOffset, int length) {
		int outByte;
		try {
			for(int i = startOffset; i < startOffset + length; i++) {
				outByte = (block[i] & 0xFF);
				out.write(outByte);
			}
		} catch(IOException e) {}
	}

}
