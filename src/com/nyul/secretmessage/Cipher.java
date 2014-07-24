package com.nyul.secretmessage;

import java.util.Locale;

public class Cipher {

	public static String encrypt(String message) {
		return message.toUpperCase(Locale.US);
	}

	public static String decrypt(String message) {
		return message.toLowerCase(Locale.US);
	}

}
