package tdd.vendingMachine.ui;

public interface IVendingMachineUI {
    public enum SupportedCoins {
        FIVE(5f), TWO(2f), ONE(1f), HALF(0.5f), ONE_FIFTH(0.2f), ONE_TEN(0.1f);
        
        float denomination;
        
        SupportedCoins(float denomination) {
            this.denomination = denomination;
        }
        
        public static SupportedCoins createSupportedCoins(float denomination) {
            for(SupportedCoins coin : SupportedCoins.values()) {
                if(coin.denomination == denomination) {
                    return coin;
                }
            }
            
            throw new IllegalArgumentException("Denomination = " + denomination + " is not supported");
        }
    }
    
    
    void selectShelf(Integer shelfNo);
}
