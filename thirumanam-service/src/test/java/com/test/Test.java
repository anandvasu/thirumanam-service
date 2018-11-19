package com.test;

import java.util.Random;

public class Test {

	public static String getRandomNumberString() {
	    // It will generate 6 digit random Number.
	    // from 0 to 999999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    // this will convert any number sequence into 6 character.
	    return String.format("%06d", number);
	}
	
	public static void main(String s[]) {
		System.out.println(getRandomNumberString());
	}
}
