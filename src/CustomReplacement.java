/**
 * Custom Replacement Policy which evicts the second MRU entry
 *
 */
public class CustomReplacement<V> implements Policy<V> {
    @Override
    public int evictEntryIndex(CacheEntry<V>[] entries, int startIndex, int endIndex) {
        int mruIndex = startIndex;
        int secondMRUIndex = startIndex;
        long mruTimeStamp = entries[startIndex].getTimeStamp();
        long secondMRUTimeStamp = Long.MIN_VALUE;

        for(int i = 0 ; i < entries.length; i++){
            if(mruTimeStamp < entries[i].getTimeStamp()) {
                secondMRUTimeStamp = mruTimeStamp;
                secondMRUIndex = mruIndex;
                mruTimeStamp = entries[i].getTimeStamp();
                mruIndex = i;
            }
            else if(secondMRUIndex < entries[i].getTimeStamp() && entries[i].getTimeStamp() != mruTimeStamp) {
                secondMRUTimeStamp = entries[i].getTimeStamp();
                secondMRUIndex = i;
            }
        }
        if(secondMRUTimeStamp == Long.MIN_VALUE) {
            return endIndex;
        }
        return secondMRUIndex;
    }
}
