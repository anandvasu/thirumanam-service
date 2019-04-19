package com.test;

import java.util.Arrays;
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
		
		String[] casteArray = { "Aeronautical Engineering",
				"B.Arch",
				"BCA",
				"BE",
				"B.Plan",
				"B.SC IT/Computer Science",
				"B.Tech",
				"B.S (Engineering)",
				"M.Arch",
				"MCA",
				"ME",
				"M.Sc IT / Computer Science",
				"M.S.(Engg)",
				"M.Tech",
				"D.Ted",
				"PGDCA",
				"Aviation Degree",
				"B.A",
				"B.Com",
				"B.Ed",
				"BFA",
				"BFT",
				"BLIS",
				"B.M.M",
				"B.Sc.",
				"B.S.W",
				"B.Phil",
				"M.A",
				"MCom",
				"M.Ed.",
				"MFA",
				"M.Sc. - Master of Science",
				"MSW",
				"M.Phil.",
				"BBA",
				"BFM - Bachelor of Financial Management",
				"BHM - Bachelor of Hotel Management",
				"BHA - Bachelor of Hospital Administration",
				"BHM - Bachelor of Hospital Management",
				"MBA",
				"MFM - Master of Financial Management",
				"MHM - Master of Hotel Management",
				"MHRM - Master of Human Resource Management",
				"PGDM",
				"B.A.M.S",
				"BDS",
				"BHMS",
				"BPharm",
				"BPT",
				"BUMS",
				"BVSc",
				"MBBS",
				"B.Sc Nursing",
				"MDS",
				"MD",
				"MS-Medical",
				"M.Pharm",
				"MPT",
				"MVSc",
				"BGL",
				"B.L",
				"LL.B",
				"LL.M",
				"M.L",
				"CA",
				"CFA - Chartered Financial Analyst",
				"CS",
				"ICWA",
				"IAS",
				"IES", 
				"IFS",
				"Diploma",
				"Polytechnic",
				"Ph.d",
				"Higher Secondary School"};
		
		Arrays.sort(casteArray);
		
		int i = 100;
		for(String caste : casteArray) {
			System.out.println( "{ value:"+ i +", label: \""+caste+"\"},");
			i = i+2;
		}

	}
}
