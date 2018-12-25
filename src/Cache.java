/**
 * Zeyuan Gu
 * 11/18/2018
 */
public interface Cache<K, V> {
    V get(final K key);
    void put(final K key, final V value);
    int size();
    void clear();
}
