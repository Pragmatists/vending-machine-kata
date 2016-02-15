package tdd.vendingMachine.Transaction;

/**
 * Exception meaning a fatal state requiring full rollback of the "buy-transaction".
 */
public class MachineException extends Exception {
    public MachineException(String message) {
        super(message);
    }
    public MachineException(String message, Throwable cause) {
        super(message, cause);
    }
}
