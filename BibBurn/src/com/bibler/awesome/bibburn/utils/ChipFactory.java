package com.bibler.awesome.bibburn.utils;

public class ChipFactory {
	
	public static final int GLS29EE010 = 0x01;
	public static final int AM29F040 = 0x02;
	
	public static Chip createChip(int chipToCreate) {
		int chipSize = 0;
		int dataBufferSize = 0;
		String chipName = "";
		switch(chipToCreate) {
		case GLS29EE010:
			chipSize = 0x20000;
			dataBufferSize = 0x80;
			chipName = "GLS29EE010";
			break;
		case AM29F040:
			chipSize = 0x80000;
			dataBufferSize = 0x400;
			chipName = "AM29F040";
			break;
		}
		return new Chip(chipSize, dataBufferSize, chipName);
	}

}
