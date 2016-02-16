package tdd.vendingMachine.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Crud repository for products (catalog of products).
 *
 * Methods follow JPA-repository semantics.
 */
public class ProductRepo {
    private AtomicInteger idGen = new AtomicInteger(0);
    private Map<Integer, Product> catalog = new HashMap<>();


    public boolean contains(Integer id) {
        return catalog.containsKey(id);
    }

    public Product findOne(Integer id) {
        return catalog.get(id);
    }

    public Product findByNameIgnoreCase(String name) {
        for(Product p : catalog.values()) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) return p;
        }
        return null;
    }

    public Iterable<Product> findAll() {
        return catalog.values();
    }

    public Product save(Product p) {
        if (p.getPid()==0 || !catalog.containsKey(p.getPid())) {
            p.setPid(idGen.incrementAndGet());
        }
        catalog.put(p.getPid(), p);
        return p;
    }

    public int count() {
        return catalog.size();
    }

    public void delete(Product p) {
        if (p==null || p.getPid()<=0) return;
        catalog.remove(p.getPid());
    }

}
