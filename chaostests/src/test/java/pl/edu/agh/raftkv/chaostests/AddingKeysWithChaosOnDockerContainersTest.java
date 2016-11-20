package pl.edu.agh.raftkv.chaostests;

import lombok.val;
import org.arquillian.cube.q.api.ContainerChaos;
import org.arquillian.cube.q.api.ContainerChaos.ContainersType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static pl.edu.agh.raftkv.chaostests.KeyValueStoreTests.*;

/**
 * Created by novy on 13.11.16.
 */

@RunWith(Arquillian.class)
public class AddingKeysWithChaosOnDockerContainersTest {

    @ArquillianResource
    ContainerChaos containerChaos;

    @Test
    public void should_survive_stopping_containers() throws Exception {
        // given
        waitForClusterToBeReady();
        val numberOfKeys = 50_000;

        // when
        containerChaos.onCubeDockerHost().stopRandomly(
                ContainersType.regularExpression("^node"),
                ContainerChaos.IntervalType.intervalInSeconds(10)
        ).exec(() -> writeNKeys(numberOfKeys));

        // expect
        assertThatNKeysHaveBeenWritten(numberOfKeys);
    }
}
