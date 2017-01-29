package main.java.tdd.vendingMachine;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
	//All the sections of the Vending Machine
	private List<ISection> sections;
	
	//The text shown in the display of the Vending Machine
	private String displayText;
	
	//Money the user has already introduced in the machine
	private float currentMoney;
	
	
	//Money accumulated in the machine, it's useful to know if we can give change or not
	private float cumulativeMoney; 
	
}


