package pl.edu.agh.raftkv.protocol;

import io.atomix.copycat.Query;
import lombok.RequiredArgsConstructor;

/**
 * Created by novy on 12.11.16.
 */

@RequiredArgsConstructor
public class GetQuery implements Query<Object> {
    public final String key;

    @Override
    public ConsistencyLevel consistency() {
        return ConsistencyLevel.LINEARIZABLE_LEASE;
    }
}
