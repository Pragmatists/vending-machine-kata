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
        assertThat(colaId).isEqualTo(1);
        assertThat(repo.count()).isEqualTo(1);
        repo.save(sprite);
        repo.save(snack);
        //tests also the operation of the `pid` generator
        assertThat(repo.count()).isEqualTo(3);
        assertThat(snack.getPid()).isEqualTo(3);
    }

    @Test
    public void updatingProducts() {
        Product cola = new Product("Cola", 12);
        repo.save(cola);
        Integer id = cola.getPid();
        cola.setPrice(13);
        repo.save(cola);
        //id should not change on update
        assertThat(cola.getPid()).isEqualTo(id);
        assertThat(repo.findOne(id).getPrice()).isEqualTo(13);
        assertThat(repo.count()).isEqualTo(1);
    }

    @Test
    public void findingProducts() {
        MockRepos.fillProductRepo(repo);
        assertThat(repo.findByNameIgnoreCase("cola")).isNotNull();
        repo.findAll().forEach((Product p)->{
            assertThat(repo.findByNameIgnoreCase(p.getName().toUpperCase())).isEqualTo(p);
        });
    }

    @Test
    public void entityRemoval() {
        MockRepos.fillProductRepo(repo);
        int okCount = repo.count();
        Product cola = repo.findByNameIgnoreCase("cola");
        repo.delete(cola);
        okCount--;
        assertThat(repo.count()).isEqualTo(okCount);
        repo.delete(cola);
        assertThat(repo.count()).isEqualTo(okCount);
        Product egg = new Product("Egg", 1);
        repo.delete(egg);
        assertThat(repo.count()).isEqualTo(okCount);
    }


}
