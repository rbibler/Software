package com.bibler.awesome.bibburn.utils;

public class Chip {
	
	private int chipSize;
	private int dataBufferSize;
	private String chipName;
	
	public Chip(int chipSize, int dataBufferSize, String chipName) {
		this.chipSize = chipSize;
		this.dataBufferSize = dataBufferSize;
		this.chipName = chipName;
	}

	
	public int getDataBufferSize() {
		return dataBufferSize;
	}
	
	public int getChipSize() {
		return chipSize;
	}

	
	public String printChipInfo() {
		StringBuilder s = new StringBuilder();
		s.append("New Chip: ");
		s.append(chipName);
		s.append("\n\t" + "Size: " + Integer.toHexString(chipSize));
		s.append("\n\t" + "Page Size: " + Integer.toHexString(dataBufferSize));
		return s.toString();
	}

}
