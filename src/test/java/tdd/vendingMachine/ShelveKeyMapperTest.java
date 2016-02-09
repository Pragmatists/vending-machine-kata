package tdd.vendingMachine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import tdd.vendingMachine.shelve.DefaultShelve;
import tdd.vendingMachine.shelve.Shelve;
import tdd.vendingMachine.shelve.ShelveKeyMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by okraskat on 06.02.16.
 */
public class ShelveKeyMapperTest {

    @Test
    public void shouldMapShelvesToKeys() throws Exception {
        //given
        Shelve shelve = new DefaultShelve<>(new ArrayList<>(), new BigDecimal("10.0"), "");
        List<Shelve> shelves = Lists.newArrayList(shelve, shelve, shelve);
        //when
        Map<Integer, Shelve> resultMap = Maps.uniqueIndex(shelves, new ShelveKeyMapper());
        //then
        assertNotNull(resultMap);
        assertEquals(3, resultMap.size());
        Iterator<Integer> iterator = resultMap.keySet().iterator();
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
    }

}
