package tdd.vendingMachine.service;

import java.util.List;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IMoneyService.SupportedCoins;

public interface IDropService {
    void dropCoins(List<SupportedCoins> coins);
    
    void dropUnknownDenomination(float denomination);
    
    void dropProduct(Product product);
}
