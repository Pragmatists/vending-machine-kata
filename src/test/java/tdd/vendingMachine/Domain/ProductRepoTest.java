package tdd.vendingMachine.Domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductRepo is just a standard JPA-like repository of Product entities.
 */

public class ProductRepoTest {
    ProductRepo repo;

    @Before
    public void setUp() {
        repo = new ProductRepo();
    }

    @Test
    public void persistingProducts() {
        Product cola = new Product("Cola", 12);
        Product sprite = new Product("Sprite", 11);
        Product snack = new Product("Snack", 8);

        repo.save(cola);
        Integer colaId = cola.getPid();
        assertThat(colaId==1);
        assertThat(repo.count() == 1);
        repo.save(sprite);
        repo.save(snack);
        assertThat(repo.count() == 3);
        assertThat(snack.getPid() == 3);
    }

    @Test
    public void updatingProducts() {
        Product cola = new Product("Cola", 12);
        repo.save(cola);
        Integer id = cola.getPid();
        cola.setPrice(13);
        repo.save(cola);
        //id should not change on update
        assertThat(cola.getPid().equals(id));
        assertThat(repo.findOne(id).getPrice().equals(13));
        assertThat(repo.count() == 1);
    }

    @Test
    public void findingProducts() {
        MockRepos.fillProductRepo(repo);
        assertThat(repo.findByNameIgnoreCase("cola") != null);
        repo.findAll().forEach((Product p)->{
            assertThat(repo.findByNameIgnoreCase(p.getName().toUpperCase())==p);
        });
    }

    @Test
    public void entityRemoval() {
        MockRepos.fillProductRepo(repo);
        int okCount = repo.count();
        Product cola = repo.findByNameIgnoreCase("cola");
        repo.delete(cola);
        okCount--;
        assertThat(repo.count() == okCount);
        repo.delete(cola);
        assertThat(repo.count() == okCount);
        Product egg = new Product("Egg", 1);
        repo.delete(egg);
        assertThat(repo.count() == okCount);
    }


}
