package pl.edu.agh.raftkv.client.raftserver;

import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.StateMachineExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by novy on 12.11.16.
 */
class RaftKVStateMachine extends StateMachine {
    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

    @Override
    protected void configure(StateMachineExecutor executor) {
        executor.register(PutCommand.class, this::put);
        executor.register(GetQuery.class, this::get);
        executor.register(DeleteCommand.class, this::delete);
    }

    private void put(Commit<PutCommand> commit) {
        final PutCommand aCommand = commit.command();
        dataStore.put(aCommand.key, aCommand.value);
        commit.close();
    }

    private Object get(Commit<GetQuery> commit) {
        final String keyToLookup = commit.query().key;
        try {
            return dataStore.get(keyToLookup);
        } finally {
            commit.close();
        }
    }

    private void delete(Commit<DeleteCommand> commit) {
        final DeleteCommand aCommand = commit.command();
        dataStore.remove(aCommand.key);
        commit.close();
    }
}

