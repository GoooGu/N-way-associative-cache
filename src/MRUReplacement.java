public class MRUReplacement<V> implements Policy<V>{
    @Override
    public int evictEntryIndex(CacheEntry<V>[] entries, int startIndex, int endIndex) {
        int mruIndex = startIndex;
        long mruTimeStamp = entries[startIndex].getTimeStamp();
        for(int i = startIndex; i <= endIndex; i++){
            if(mruTimeStamp < entries[i].getTimeStamp()){
                mruIndex = i;
                mruTimeStamp = entries[i].getTimeStamp();
            }
        }
        return mruIndex;
    }
}
