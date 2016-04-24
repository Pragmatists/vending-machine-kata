package tdd.vendingMachine.Domain;


/**
 * Filling repositories with some data
 */
public class MockRepos {
    public static void fillProductRepo(ProductRepo repo) {
        repo.save(new Product("Cola", 10));
        repo.save(new Product("Sprite", 11));
        repo.save(new Product("Snack", 8));
    }

    //assumes pid={1,2,3} are valid; and srepo==null
    public static StorageRepo createFillStorageRepo() {
        StorageRepo srepo = new StorageRepo(5, 20);
        srepo.setProductAtShelf(0, 1, 5);
        srepo.setProductAtShelf(1, 1, 1);
        srepo.setProductAtShelf(2, 1, 0);
        srepo.setProductAtShelf(3, 2, 5);
        srepo.setProductAtShelf(4, 3, 5);
        return srepo;
    }
}
