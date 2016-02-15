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
}
