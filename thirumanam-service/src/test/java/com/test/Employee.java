package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Employee {

	private String name;
	private int age;
	
	Employee(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}	
	
	public static void main(String s[]) {
		Employee ravi = new Employee("Ravi", 20);
		Employee anand = new Employee("Anand", 20);
		Employee pushpa = new Employee("Pushpa", 20);
		Employee arun = new Employee("Arun", 20);
		
		
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(ravi);
		employees.add(anand);
		employees.add(pushpa);
		employees.add(arun);
		
		/*//Collections.sort(employees, (emp1, emp2) -> emp1.getName().compareTo(emp2.getName()));
		employees.stream().forEach((emp) -> {
			System.out.println(emp.getName());
		});;*/
	
		//System.out.println(employees.stream().filter((emp) -> emp.getName().startsWith("A")).count());		
	
		List<Employee> empList = employees.stream().filter((emp) -> emp.getName().startsWith("A")).collect(Collectors.toList());
		System.out.println(empList.size());
		
				
		/*List<Double> integerList = new ArrayList<Double>();
		integerList.add(1.2);
		integerList.add(5.2);
		integerList.add(8.2);
		integerList.add(9.2);
		
		Optional<Double> result = integerList.parallelStream().max(Double::compare);
		integerList.stream().limit(2).forEach((value) -> System.out.println(value));
		
		if(result.isPresent()) {
			System.out.println(result.get());
		}*/
		
		
	}
}