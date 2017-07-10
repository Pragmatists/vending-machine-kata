package tdd.vendingmachine.domain;

import lombok.ToString;
import tdd.vendingmachine.domain.dto.ShelfDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
class Shelves {

    private final Map<ShelfNumber, Shelf> shelvesByNumber;

    private Shelves(Map<ShelfNumber, Shelf> shelvesByNumber) {
        this.shelvesByNumber = Collections.unmodifiableMap(Objects.requireNonNull(shelvesByNumber));
    }

    static Shelves create(Set<ShelfDto> shelves) {
        Map<ShelfNumber, Shelf> shelvesByNumber = shelves
            .stream()
            .collect(Collectors.toMap(shelfDto -> new ShelfNumber(shelfDto.getShelfNumber()), Shelf::create));
        return new Shelves(shelvesByNumber);
    }

    Optional<Shelf> findBy(ShelfNumber shelfNumber) {
        return Optional.ofNullable(shelvesByNumber.get(shelfNumber));
    }

    Shelves removeProductFromShelf(ShelfNumber shelfNumber) {
        Shelf updatedShelf = findBy(shelfNumber).map(Shelf::removeSingleProduct)
                                                .orElseThrow(IllegalArgumentException::new);
        Map<ShelfNumber, Shelf> copyShelvesByNumber = new HashMap<>(this.shelvesByNumber);
        copyShelvesByNumber.put(shelfNumber, updatedShelf);
        return new Shelves(copyShelvesByNumber);
    }
}
