public class LRUReplacement<V> implements Policy<V> {
    @Override
    public int evictEntryIndex(CacheEntry<V>[] entries, int startIndex, int endIndex) {
        int lruIndex = startIndex;
        long lruTimeStamp = entries[startIndex].getTimeStamp();
        for(int i = startIndex; i <= endIndex; i++){
            if(lruTimeStamp > entries[i].getTimeStamp()){
                lruIndex = i;
                lruTimeStamp = entries[i].getTimeStamp();
            }
        }
        return lruIndex;
    }
}
