package main.java.tdd.vendingMachine;

public class TestMain {
	
	
	static void checkValues(VendingMachine vm, float currentMoney, int[] cumulativeMoney, String displayText){
		//CURRENT MONEY
		if (vm.getCurrentMoney()!=currentMoney){
			System.out.println("ERROR: Expected current money:"+currentMoney+" received:"+vm.getCurrentMoney());
		} else {
			System.out.println("PASSED: Expected current money:"+currentMoney+" received:"+vm.getCurrentMoney());
		}
		
		//CUMULATIVE MONEY
		int[] vmCumulativeMoney=vm.getCumulativeMoney();
		boolean areEquals= true;
		for (int i=0;i<vm.sizeCumulativeMoney;i++){
			if (cumulativeMoney[i]!=vmCumulativeMoney[i]){
				areEquals=false;
			}
		}
		if (!(areEquals)){
			System.out.println("ERROR: Expected Cumulative money different from received");
		} else {
			System.out.println("PASSED: Expected Cumulative money same as received");
		}
		
		//DISPLAY TEXT
		if (!(vm.getDisplayText().equals(displayText))){
			System.out.println("ERROR: Expected display text:"+displayText+" received:"+vm.getDisplayText());
		} else {
			System.out.println("PASSED: Expected display text:"+displayText+" received:"+vm.getDisplayText());
		}
	}
	
	static public void main(String[] args){
		System.out.println("Testing the system");
		VendingMachine vm=new VendingMachine();
	
		//TESTING THE SYSTEM
		//I click on Coca Cola, so I see the price of Coca Cola (1.5)
		vm.selectISection(0);
		int[] tmpCumulativeArray0={0,0,0,0,0,0};
		checkValues(vm,0f,tmpCumulativeArray0,"1.5");
		
		vm.insertCoin(1);
		int[] tmpCumulativeArray={0,0,0,1,0,0};
		checkValues(vm,1f,tmpCumulativeArray,"1.0");
		
		//I want a CocaCola, but nothing happens, there is not enough credit
		vm.selectISection(0);
		checkValues(vm,1f,tmpCumulativeArray,"1.0");
		
		//I add 0.5 and select a Fanta, there is not enough change, so it returns the money
		vm.insertCoin(0.5f);
		vm.selectISection(1);
		int[] tmpCumulativeArray2={0,0,0,0,0,0};
		checkValues(vm,0f,tmpCumulativeArray2,"NO AVAILABLE CHANGE!");
		
		//Now I add 1 coin of 1 and 1 coin of 0.5, it should appear 1.5 in the screen
		vm.insertCoin(1f);
		vm.insertCoin(0.5f);
		int[] tmpCumulativeArray3={0,0,1,1,0,0};
		checkValues(vm,1.5f,tmpCumulativeArray3,"1.5");
		
		//Now I select a CocaCola, it should give me the CocaCola
		vm.selectISection(0);
		checkValues(vm,0,tmpCumulativeArray3,"DELIVERED!");
		
		//Now I add a coin of 2
		vm.insertCoin(2f);
		int[] tmpCumulativeArray4={0,0,1,1,1,0};
		checkValues(vm,2f,tmpCumulativeArray4,"2.0");

		//Now I select a CocaCola, it should give me the CocaCola and return a coin of 0,5
		vm.selectISection(0);
		int[] tmpCumulativeArray5={0,0,0,1,1,0};
		checkValues(vm,0f,tmpCumulativeArray5,"DELIVERED!");

		//Now I insert a coin of 5
		vm.insertCoin(5f);
		int[] tmpCumulativeArray6={0,0,0,1,1,1};
		checkValues(vm,5f,tmpCumulativeArray6,"5.0");
		
		//Now I select a Fanta, it should return the money because there is not enough change
		vm.selectISection(1);
		int[] tmpCumulativeArray7={0,0,0,1,1,0};
		checkValues(vm,0f,tmpCumulativeArray7,"NO AVAILABLE CHANGE!");

		//Now I add a coin of 2 and a coin of 0,5
		vm.insertCoin(2f);
		vm.insertCoin(0.5f);
		int[] tmpCumulativeArray8={0,0,1,1,2,0};
		checkValues(vm,2.5f,tmpCumulativeArray8,"2.5");
		
		//Now I select Coca Cola, however we already consumed all of them, so it should warn there are no more available
		vm.selectISection(0);
		checkValues(vm,2.5f,tmpCumulativeArray8,"THERE ARE NO PRODUCTS AVAILABLE");

		//Now I insert a coin of 5
		vm.insertCoin(5f);
		int[] tmpCumulativeArray9={0,0,1,1,2,1};
		checkValues(vm,7.5f,tmpCumulativeArray9,"7.5");
		
		//Now I ask for Aquarius
		vm.selectISection(2);
		int[] tmpCumulativeArray10={0,0,0,0,2,1};
		checkValues(vm,0f,tmpCumulativeArray10,"DELIVERED!");
		
		//Now I add 1 coin of 1 and 1 coin of 0.5 
		vm.insertCoin(1f);
		vm.insertCoin(0.5f);
		int[] tmpCumulativeArray11={0,0,1,1,2,1};
		checkValues(vm,1.5f,tmpCumulativeArray11,"1.5");
		
		//Now I cancel the operation
		vm.cancelOperation();
		int[] tmpCumulativeArray12={0,0,0,0,2,1};
		checkValues(vm,0f,tmpCumulativeArray12,"CANCELLED!");

	}
}
