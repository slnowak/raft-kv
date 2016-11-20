package pl.edu.agh.raftkv.chaostests;

import lombok.val;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static pl.edu.agh.raftkv.chaostests.KeyValueStoreTests.*;

/**
 * Created by novy on 20.11.16.
 */

@RunWith(Arquillian.class)
public class AddingKeysWithoutAnyInterruptionsTest {

    @Test
    public void should_write_n_keys_without_any_interruptions() throws Exception {
        // given
        waitForClusterToBeReady();
        val numberOfKeys = 50_000;

        // when
        writeNKeys(numberOfKeys);

        // expect
        assertThatNKeysHaveBeenWritten(numberOfKeys);
    }
}
