package tdd.vendingMachine.service;

public interface IDisplayService {
    enum MessageType {
        INFO, WARN, ERROR;
    }

    void print(String msg, MessageType type);
}
