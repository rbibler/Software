package com.bibler.awesome.bibburn.burnutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bibler.awesome.bibburn.serialutils.SerialPortInstance;
import com.bibler.awesome.bibburn.utils.TimeUtils;

public class BurnManager  {
	
	private int[] dataBuffer;
	private int[] readBuffer;
	private SerialPortInstance serialPort;
	private int chipSize = 0x80000;
	private int dataBufferSize = 0x400;
	private int burnState;
	private int currentAddress;
	private int errorCount;
	private long startTime;
	
	final int WRITE_CMD = '!';
	final int READ_CMD = '%';
	final int IDLE_MODE = 0x00;
	final int WRITE_MSG_SENT = 0x01;
	final int WRITING = 0x02;
	final int READING = 0x03;
	final int SUCCESS_MSG = 0x01;
	final int FAIL_MSG = 0x00;
	final int DONE = 0x03;
	
	public BurnManager(SerialPortInstance serialPort) {
		this.serialPort = serialPort;
		this.serialPort.setBurnManager(this);
		initializeBuffers();
	}
	
	private void initializeBuffers() {
		dataBuffer = new int[chipSize];
		readBuffer = new int[chipSize];
	}
	
	public void burnData(File f) {
		openFile(f);
		serialPort.write(WRITE_CMD);
		burnState = WRITE_MSG_SENT;
	}
	
	private void beginBurn() {
		System.out.println("Beginning Burn");
		startTime = System.currentTimeMillis();
		burnState = WRITING;
		burnNextBlock();
	}
	
	private void beginRead() {
		System.out.println("Beginning Read");
		errorCount = 0;
		currentAddress = 0;
		startTime = System.currentTimeMillis();
		burnState = READING;
		serialPort.write(READ_CMD);
	}
	
	private void burnNextBlock() {
		if(currentAddress >= chipSize) {
			endBurn(DONE);
			return;
		}
		serialPort.writeBlock(dataBuffer, currentAddress, dataBufferSize);
		currentAddress += dataBufferSize;
		System.out.print("Address - " + Integer.toHexString(currentAddress));
		System.out.print(" Percent - " + (float)currentAddress / (float)chipSize *  100); 
		System.out.println(" Time Elapsed - " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime));
		
	}
	
	private void endBurn(int message) {
		currentAddress = 0;
		burnState = IDLE_MODE;
		switch(message) {
		case DONE:
			System.out.println("Burn complete! Time elapsed " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime));
			beginRead();
			break;
		case FAIL_MSG:
			System.out.println("Burn failed! Current Address is " + Integer.toHexString(currentAddress));
			break;
		}
		
	}
	
	public void receiveSerialData(int dataReceived) {
		switch(burnState) {
		case IDLE_MODE:
			break;
		case WRITE_MSG_SENT:
			if(dataReceived == SUCCESS_MSG) {
				beginBurn();
			} else {
				System.out.println("Write Command Failed");
			}
			break;
		case WRITING:
			if(dataReceived == SUCCESS_MSG) {
				burnNextBlock();
			} else {
				endBurn(FAIL_MSG);
			}
			break;
		case READING:
			if(dataBuffer[currentAddress] != dataReceived) {
				errorCount++;
			}
			readBuffer[currentAddress++] = dataReceived;
			System.out.print("Address - " + Integer.toHexString(currentAddress));
			System.out.print(" Percent - " + (float)currentAddress / (float)chipSize *  100); 
			System.out.println(" Time Elapsed - " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime));
			if(currentAddress >= chipSize) {
				System.out.println("Read complete! Time elapsed " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime));
				System.out.println("There were " + errorCount + " errors!");
				burnState = IDLE_MODE;
				currentAddress = 0;
				saveReadFile();
			}
			break;
		}
	}
	
	private void saveReadFile() {
		File f = new File("C:/users/ryan/desktop/read.bin");
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(f));
			for(int i = 0; i < readBuffer.length; i++) {
				out.write(readBuffer[i]);
			}
		} catch(IOException e) {}
		finally {
			if(out != null) {
				try {
					out.close();
				} catch(IOException e) {}
			}
		}
	}
	
	private void openFile(File f) {
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(f));
			for(int i = 0; i < dataBuffer.length; i++) {
				dataBuffer[i] = input.read() & 0xFF;
			}
		} catch(IOException e) {}
		finally {
			if(input != null) {
				try {
					input.close();
				} catch(IOException e) {}
			}
		}
	}

}
