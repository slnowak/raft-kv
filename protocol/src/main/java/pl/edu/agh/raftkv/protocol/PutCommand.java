package pl.edu.agh.raftkv.protocol;

import io.atomix.copycat.Command;
import lombok.RequiredArgsConstructor;

/**
 * Created by novy on 12.11.16.
 */
@RequiredArgsConstructor
public class PutCommand implements Command<Void> {
    public final String key;
    public final Object value;

    @Override
    public CompactionMode compaction() {
        return CompactionMode.QUORUM;
    }
}
