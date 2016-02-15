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


    boolean contains(Integer id) {
        return catalog.containsKey(id);
    }

    Product findOne(Integer id) {
        return catalog.get(id);
    }

    Product findByNameIgnoreCase(String name) {
        for(Product p : catalog.values()) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) return p;
        }
        return null;
    }

    Iterable<Product> findAll() {
        return catalog.values();
    }

    Product save(Product p) {
        if (p.getPid()==0 || !catalog.containsKey(p.getPid())) {
            p.setPid(idGen.incrementAndGet());
        }
        catalog.put(p.getPid(), p);
        return p;
    }

    int count() {
        return catalog.size();
    }

    void delete(Product p) {
        if (p==null || p.getPid()<=0) return;
        catalog.remove(p.getPid());
    }

}
