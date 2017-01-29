package main.java.tdd.vendingMachine;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
	//All the sections of the Vending Machine
	private List<ISection> sections=new ArrayList<ISection>();
	
	//The text shown in the display of the Vending Machine
	private String displayText;
	
	//Money the user has already introduced in the machine
	private float currentMoney;
	
	//It indicates the number of different coins which are accepted
	final int sizeCumulativeMoney=6;
	
	//Money accumulated in the machine, it's useful to know if we can give change or not
	//Position 0, # coins of 0.1
	//Position 1, # coins of 0.2
	//Position 2, # coins of 0.5
	//Position 3, # coins of 1
	//Position 4, # coins of 2
	//Position 5, # coins of 5
	private int[] cumulativeMoney= new int[sizeCumulativeMoney]; 

	VendingMachine(){
		//I create the products
		IProduct p1= new CocaCola();
		IProduct p2=new Fanta();
		IProduct p3=new Aquarius();
		
		//I put the products in shelves(Sections)
		sections.add(new Shelve(p1,2,1));
		sections.add(new Shelve(p2,3,2));
		sections.add(new Shelve(p3,10,3));
		
		displayText="";
		currentMoney=0;
		
		//We assume that initially there is no money in the machine
		for (int i=0;i<sizeCumulativeMoney;i++){
			cumulativeMoney[i]=0;
		}
	}

	public void insertCoin (float value){
		setCurrentMoney(getCurrentMoney()+value);
		setDisplayText(Float.toString(getCurrentMoney()));
		
		if (value==0.1){
			cumulativeMoney[0]++;
		}
		if (value==0.2){
			cumulativeMoney[1]++;
		}
		if (value==0.5){
			cumulativeMoney[2]++;
		}
		if (value==1){
			cumulativeMoney[3]++;
		}
		if (value==2){
			cumulativeMoney[4]++;
		}
		if (value==5){
			cumulativeMoney[5]++;
		}
	}
	
	// The user selects a Section with the keyboard. 
	// If there is no money previously introduced, The Vending machine is supposed to show the price of the product of that Section
	// If there is money previously introduced, The Vending machine is supposed to give the product only if the amount is exactly
	// the price OR we have enough money to give the corresponding change
	public void selectISection(int iSection){
		float priceOfProduct=getSections().get(iSection).getIProduct().getPrice();
		
		//If there is no money, we just show the price of the selected product
		if (getCurrentMoney()==0){
			setDisplayText(Float.toString(priceOfProduct));
		}
		else{
			//If there is more money than needed or equal, we check if we the product is available and if there is enough change. If we do, we deliver. If we don't, we give back the money.
			if (getCurrentMoney()>=priceOfProduct){
				//We check if there are available products
				if (getSections().get(iSection).getNumberOfProducts()<=0){
					setDisplayText("THERE ARE NO PRODUCTS AVAILABLE");
				}
				else{
					if (returnMoneyToUserIfAvailable(getCurrentMoney()-priceOfProduct)){	
						//We deliver the product
						getSections().get(iSection).setNumberOfProducts(getSections().get(iSection).getNumberOfProducts()-1);
						setDisplayText("DELIVERED!");
					} else {
						//We don't have enough change, so we don't deliver the product and we give back the money
						returnMoneyToUserIfAvailable(getCurrentMoney());
						setDisplayText("NO AVAILABLE CHANGE!");
					}
				}
			}
		}
	}

	//The machine must return some money to the user, but it will only do it if it has the exact amount
	//Return true means it has the exact amount
	//Return false means it doesn't have the exact amount, so it will not give it back
	public boolean returnMoneyToUserIfAvailable(float amountToReturn){
		//Initialize the temporary structure to check if I have enough change
		int [] tmpMoney=new int[sizeCumulativeMoney];
		for (int i=0;i<sizeCumulativeMoney;i++){
			tmpMoney[i]=0;
		}
		while (amountToReturn>=5 && cumulativeMoney[5]>0){
			tmpMoney[5]++;
			amountToReturn-=5;
		}
		while (amountToReturn>=2 && cumulativeMoney[4]>0){
			tmpMoney[4]++;
			amountToReturn-=2;
		}
		while (amountToReturn>=1 && cumulativeMoney[3]>0){
			tmpMoney[3]++;
			amountToReturn-=1;
		}
		while (amountToReturn>=0.5 && cumulativeMoney[2]>0){
			tmpMoney[2]++;
			amountToReturn-=0.5;
		}
		while (amountToReturn>=0.2 && cumulativeMoney[1]>0){
			tmpMoney[1]++;
			amountToReturn-=0.2;
		}
		while (amountToReturn>=0.1 && cumulativeMoney[0]>0){
			tmpMoney[0]++;
			amountToReturn-=0.1;
		}
		
		//Just check we have the enough money to return the change and which coins we will give back
		if (amountToReturn==0){
			for (int i=0;i<sizeCumulativeMoney;i++){
				cumulativeMoney[i]-=tmpMoney[i];
			}
			//Money physically is returned to the user
			setCurrentMoney(0);
			return true;
		}
		return false;
	}
	
	//The user cancels the operation, so the user must recover the money he/she has already introduced
	public void cancelOperation(){
		returnMoneyToUserIfAvailable(getCurrentMoney());
		setDisplayText("CANCELLED!");
	}
	
	//When the owner of the Vending machine goes to the machine to get the money accumulated
	public void getMoneyFromMachineOperation(){
		for (int i=0;i<sizeCumulativeMoney;i++){
			cumulativeMoney[i]=0;
		}
	}
	
	//Public Getters and Setters
	public List<ISection> getSections() {
		return sections;
	}
	public void setSections(List<ISection> sections) {
		this.sections = sections;
	}
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public float getCurrentMoney() {
		return currentMoney;
	}
	public void setCurrentMoney(float currentMoney) {
		this.currentMoney = currentMoney;
	}
	public int[] getCumulativeMoney() {
		return cumulativeMoney;
	}
	public void setCumulativeMoney(int[] cumulativeMoney) {
		this.cumulativeMoney = cumulativeMoney;
	}
	
}


