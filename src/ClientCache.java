/**
 * Zeyuan Gu
 * 11/18/2018
 */
public class ClientCache<K, V> extends NSetCache<K, V>{
    public ClientCache(int numSet, int numEntry, Policy<V> replacementStrategy) {
        super(numSet, numEntry, replacementStrategy);
    }

    @Override
    public int getHash(Object key) {
        return Math.abs(key.hashCode());
    }
}
