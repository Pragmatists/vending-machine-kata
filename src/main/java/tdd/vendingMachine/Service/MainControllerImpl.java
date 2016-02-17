package tdd.vendingMachine.Service;

/**
 * The main implementation of this vending machine.
 * Allows Client- and Admin- operations.
 */
public class MainControllerImpl implements MachineClientController {
    TransactionServiceImpl transaction;

    @Override
    public Response insertCoin(int nominal) {
        //must be in transaction

        //Stub of implementation
//        if (!transaction.isInTransaction())
//            return new Response(ResponseMessage.PRODUCT_NOT_SELECTED.toString());
//        try {
//            transaction.insertCoin(nominal);
//        } catch (RuntimeException e) {
//            return new Response(e.toString()); //need better verbal message
//        }
//        if (transaction.isReadyForCommit()) {
//            transaction.commit();
//            Response r = new Response();
//            r.setResult(ResponseMessage.PRODUCT_SERVED_CHANGE_DISBURSED);
//        }
//        Response r = new Response();
//        r.setResult(ResponseMessage.COIN_INSERTED_MORE_NEEDED);
        return null;
    }


    @Override
    public Response getShelfContents() {
        return null;
    }

    @Override
    public Response selectShelf(int shelf) {
        return null;
    }

    @Override
    public Response cancelTransaction() {
        return null;
    }
}

