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
		
		String[] casteArray = { "--ADMIN--",
				"Administrative Professional",
				"Clerk",
				"Executive",
				"Human Resource Professional - HR",
				"Manager",
				"Officer",
				"Supervisor",
				"--AGRICULTURE--",
				"Agriculture & Farming Professional",
				"--AIRLINE--",
				"Pilot",
				"Air Hostes",
				"Airline Professional",
				"Aiport Services",
				"--ARCHITECT--",
				"Architect",
				"Interior Designer",
				"--BANKING & FINANCE--",
				"Accounts Finance Professional",
				"Aditor",
				"Banking Service Professional",
				"Charted Accountant",
				"Company Secretary",
				"Financial Accountant",
				"Financial Analyst/Planning",
				"--BEAUTY & FASHION--",
				"Beautician",
				"Fashion Designer",
				"--CIVIL SERVICES--",
				"Civil Services -IAS",
				"Civil Services -IES",
				"Civil Services -IFS",
				"Civil Services -IPS",
				"Civil Services -IRS",
				"--DEFENCE--",
				"Army",
				"Navy",
				"Airforce",
				"--EDUCATION--",
				"Education Professional",
				"Professor/Lecturer",
				"Teaching/Academician",
				"--HOSPITALITY--",
				"Hotel/Hospitality Professional",
				"--IT & ENGINEERING",
				"Designer",
				"Engineer - Non IT",
				"Hardware Professional",
				"Software Professional",
				"--LEGAL--",
				"Lawyer & Legal Professinal",
				"--MEDICAL--",
				"Doctor",
				"Healthcare Professional",
				"Nurse",
				"Paramedical Professional",
				"--MARKETING & SALES--",
				"Marketing Professional",
				"Sales Professional",
				"--MEDIA & ENTERTAINMENT--",
				"Advertising/PR Professional",
				"Designer",
				"Entertainment Professional",
				"Journalist",
				"Media Professional",
				"--MERCHANT NAVY--",
				"Mariner/Merchant Navy",
				"--SCIENTIST--",
				"Scientist/Researcher",
				"--TOP MANAGEMENT--",
				"CEO/President/Chairman/Director",
				"--OTHER PROFESSIONS--",
				"Arts/Craftsman",
				"Business Owner/Entrepreneur",
				"Consultant",
				"Customer Care Professional",
				"Labour",
				"Other",
				"Social Worker",
				"Sportsman",
				"Technician"};
		
		//Arrays.sort(casteArray);
		
		int i = 5;
		for(String caste : casteArray) {
			System.out.println( "{ value:"+ i +", label: \""+caste+"\"},");
			i = i+5;
		}

	}
}
