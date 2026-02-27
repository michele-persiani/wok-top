package it.unibo.msrehab.rl.history;

/**
 * This interface defines the contract for persisting and retrieving the state transitions
 * managed by a {@code HistoryManager}. Implementations are responsible for providing
 * mechanisms to store and load history in a manner appropriate for their persistence medium.
 *
 * @param <S> the type representing the state in the history
 * @param <A> the type representing the actions in the history
 */
public interface IHistoryPersistence<S, A>
{
    /**
     * Saves the current state of the provided {@code HistoryManager} into persistent storage.
     * The behavior can be controlled to either append the current history to the existing
     * persisted history or to overwrite it entirely.
     *
     * @param history the {@code HistoryManager} instance containing the history to persist
     * @param append a boolean flag indicating whether to append (if {@code true}) the current
     *               history to the existing persisted history or to overwrite the existing
     *               persisted history entirely (if {@code false})
     * @return {@code true} if the history was successfully saved, {@code false} otherwise
     */
    boolean save(HistoryManager<S, A> history, boolean append);

    /**
     * Loads the persisted history into the provided {@code HistoryManager} instance.
     * The behavior can be controlled to either append the loaded history to the existing
     * history or replace it entirely.
     *
     * @param history the {@code HistoryManager} instance where the persisted history will be loaded
     * @param append a boolean flag indicating whether to append (if {@code true}) the loaded history
     *               to the existing history or to clear the existing history first (if {@code false})
     * @return {@code true} if the history was successfully loaded, {@code false} otherwise
     */
    boolean load(HistoryManager<S, A> history, boolean append);

    /**
     * Creates a {@code IHistoryPersistence} instance that does not persist nor retrieve anything.
     *
     * @return instance that does not persist nor retrieve anything.
     */
    static <S, A> IHistoryPersistence<S, A> noPersistence()
    {
        return new IHistoryPersistence<S, A>()
        {
            @Override
            public boolean save(HistoryManager<S, A> history, boolean append)
            {
                return true;
            }

            @Override
            public boolean load(HistoryManager<S, A> history, boolean append)
            {
                return true;
            }
        };
    }
}
