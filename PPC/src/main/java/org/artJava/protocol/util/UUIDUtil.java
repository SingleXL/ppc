package org.artJava.protocol.util;

import java.util.Random;

public class UUIDUtil {

	public static String randomID() {
		char[] chars = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
		Random r = new Random();
		char[] randomHead = new char[5];
		for (int i = 0; i < 5; i++) {
			randomHead[i] = chars[r.nextInt(chars.length)];
		}
		return AddressUtil.getInstance().getIPAddress() + "_" + new String(randomHead) + "";
	}
	
}
