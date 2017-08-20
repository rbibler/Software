package com.bibler.awesome.bibburn.burnutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.bibler.awesome.bibburn.serialutils.SerialPortInstance;
import com.bibler.awesome.bibburn.utils.TimeUtils;
import com.bibler.awesome.bibburner.ui.MainFrame;

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
	
	private MainFrame mainFrame;
	
	public BurnManager(SerialPortInstance serialPort) {
		this.serialPort = serialPort;
		if(serialPort != null) {
			this.serialPort.setBurnManager(this);
		}
		initializeBuffers();
	}
	
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	private void initializeBuffers() {
		dataBuffer = new int[chipSize];
		readBuffer = new int[chipSize];
	}
	
	public void loadFile(File f) {
		openFile(f);
	}
	
	public void sendBurnCommand() {
		serialPort.write(WRITE_CMD);
		burnState = WRITE_MSG_SENT;
		mainFrame.updateMessageArea("Sent Write Command!!");
	}
	
	private void beginBurn() {
		mainFrame.updateMessageArea("Beginning Burn!");
		startTime = System.currentTimeMillis();
		burnState = WRITING;
		burnNextBlock();
	}
	
	private void beginRead() {
		mainFrame.updateMessageArea("Beginning Read!");
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
		long timeElapsed = System.currentTimeMillis() - startTime;
		float percentComplete = (float) currentAddress / (float) chipSize;
		mainFrame.updateBurnProgress(percentComplete, timeElapsed, calculateTotalTime(timeElapsed));
		
	}
	
	private void endBurn(int message) {
		currentAddress = 0;
		burnState = IDLE_MODE;
		switch(message) {
		case DONE:
			mainFrame.updateMessageArea("Burn complete! Time elapsed " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime, false));
			beginRead();
			break;
		case FAIL_MSG:
			mainFrame.updateMessageArea("Burn failed! Current Address is " + Integer.toHexString(currentAddress));
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
				mainFrame.updateMessageArea("Write Command Failed");
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
			long timeElapsed = System.currentTimeMillis() - startTime;
			float percentComplete = (float) currentAddress / (float) chipSize;
			mainFrame.updateBurnProgress(percentComplete, timeElapsed, calculateTotalTime(timeElapsed));
			if(currentAddress >= chipSize) {
				mainFrame.updateMessageArea("Read complete! Time elapsed " + TimeUtils.millisToMinutes(System.currentTimeMillis() - startTime, false));
				mainFrame.updateMessageArea("There were " + errorCount + " errors!");
				burnState = IDLE_MODE;
				currentAddress = 0;
				saveReadFile();
			}
			break;
		}
	}
	
	private long calculateTotalTime(long timeElapsed) {
		return (long) (((float) timeElapsed / (float) currentAddress) * (chipSize - currentAddress));
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
		mainFrame.updateMessageArea("Loaded file! " + f.getName());
	}

}
