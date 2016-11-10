package tdd.vendingMachine.service;

import java.util.List;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.service.IMoneyService.SupportedCoins;

public interface IDropService extends Confirmable {
    void addToDrop(List<SupportedCoins> coins);
    
    void addToDrop(float denomination);
    
    void addToDrop(Product product);
}
