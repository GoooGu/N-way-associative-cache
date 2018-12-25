/**
 * Eviction Policy Interface ----Users can define multiple custom replacement policies from this interface
 */
public interface Policy <V>{
    int evictEntryIndex(CacheEntry<V>[] entries, int startIndex, int endIndex);
}
