package szczepanski.gerard.vendingmacinekata.domain.vendingmachine;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * Value object that represents unique id in the system.
 */
@Value
@AllArgsConstructor(access = PRIVATE)
public class Id {

    private final String value;

    public static Id generate() {
        return new Id(UUID
                        .randomUUID()
                        .toString()
                        .replaceAll("-", ""));
    }

    public String plainValue() {
        return value;
    }

}
