package com.nyul.secretmessage;

import java.util.Locale;

public class Cipher {
	
	
	public static String encrypt(String message) {
		char[] a = message.toCharArray();

		for(int i=0; i < a.length;  i++){
			a[i] = (char)( a[i] + 1);
		}
		return new String(a);	
	}

	public static String decrypt(String message) {
		char[] a = message.toCharArray();

		for(int i=0; i < a.length;  i++){
			a[i] = (char)( a[i] - 1);
		}
		return new String(a);	
	}

	
}
