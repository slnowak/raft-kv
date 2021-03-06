package pl.edu.agh.raftkv.protocol;

import io.atomix.copycat.Command;
import lombok.RequiredArgsConstructor;

/**
 * Created by novy on 12.11.16.
 */

@RequiredArgsConstructor
public class DeleteCommand implements Command<Void> {
    public final String key;

    @Override
    public CompactionMode compaction() {
        return CompactionMode.TOMBSTONE;
    }
}

