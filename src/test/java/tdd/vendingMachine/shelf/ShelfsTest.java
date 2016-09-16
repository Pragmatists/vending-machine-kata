package tdd.vendingMachine.shelf;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShelfsTest {

    Shelfs shelfs = new Shelfs();

    @Test
    public void shouldContainsShelfs() throws Exception {
        //given
        //when
        //then
        assertThat(shelfs.size()).isEqualTo(Shelfs.SHELFS_NUMBER);
    }
}
