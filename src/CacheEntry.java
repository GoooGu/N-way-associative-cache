/**
 * Zeyuan Gu
 * 11/18/2018
 */

public class CacheEntry<V>{
    public int tag;
    public V data;
    public boolean isValid;
    public long timeStamp;

    public CacheEntry() {
        this.tag = 0;
        this.data = null;
        this.isValid = true;
        this.timeStamp = 0;
    }

    public CacheEntry(int tag, V data, boolean isValid, long timeStamp) {
        this.tag = tag;
        this.data = data;
        this.isValid = isValid;
        this.timeStamp = timeStamp;
    }

    public int getTag() {
        return this.tag;
    }

    public V getData() {
        return this.data;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setData(V data) {
        this.data = data;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
