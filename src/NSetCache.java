/*
* This class is a superclass that can be extended by clients. It comes with its own lru/mru Replacement Algorithms
*
* Zeyuan Gu
* 11/18/2018
* */

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NSetCache<K, V> implements Cache<K, V>{

    protected final CacheEntry<V>[] cacheArray;
    protected final Policy<V> replacementStrategy;
    private final int numSet;
    private final int numEntry;

    /**
     * Default Contructor with Direct Mapping
     * @param numSet
     */
    @SuppressWarnings({"unchecked"})
    public NSetCache(int numSet) {
        this.numSet = numSet;
        this.numEntry = 1;
        this.replacementStrategy = new LRUReplacement<V>();
        cacheArray = (CacheEntry<V>[]) Array.newInstance(CacheEntry.class, this.numEntry * this.numSet);
        clear();
    }

    /**
     * Contructor with Default Replacement Algorithm set to LRU
     * @param numSet
     * @param numEntry
     */
    @SuppressWarnings({"unchecked"})
    public NSetCache(int numSet, int numEntry) {
        this.numSet = numSet;
        this.numEntry = numEntry;
        this.replacementStrategy = new LRUReplacement<V>();
        cacheArray = (CacheEntry<V>[]) Array.newInstance(CacheEntry.class, this.numEntry * this.numSet);
        clear();
    }

    /**
     * Constructor with client customizable Replacement Algorithm
     * @param numSet
     * @param numEntry
     * @param replacementStrat
     */
    @SuppressWarnings({"unchecked"})
    public NSetCache(int numSet, int numEntry, Policy<V> replacementStrat) {
        this.numSet = numSet;
        this.numEntry = numEntry;
        this.replacementStrategy = replacementStrat;
        cacheArray = (CacheEntry<V>[]) Array.newInstance(CacheEntry.class, this.numEntry * this.numSet);
        clear();
    }

    /**
     * If the key exists in the cache, update the timeStamp and return the value
     * @param key
     * @return value/null
     */
    public final V get(K key) {
        int intKey = getHash(key);
        int startIndex = getStartIndex(intKey);
        int endIndex = getEndIndex(startIndex);


        for(int i = startIndex; i <= endIndex; i++){
            if(cacheArray[i].getTag() == intKey){
                cacheArray[i].setTimeStamp(getCurrentTime());
                return cacheArray[i].getData();
            }
        }
        return null;
    }

    /**
     * Put the value in the cache. If the key already exists, update its data and timeStamp
     * @param key
     * @param value
     */
    public final void put(K key, V value) {
        int intKey = getHash(key);
        int startIndex = getStartIndex(intKey);
        int endIndex = getEndIndex(startIndex);

        int validIndex = startIndex;
        boolean isEntryValid = false;
        boolean entryUpdated = false;
        for(int i = startIndex; i <= endIndex; i++){
            if(cacheArray[i].isValid() && !isEntryValid){
                validIndex = i;
                isEntryValid = true;
                continue;
            }

            if(cacheArray[i].getTag() == intKey){
                cacheArray[i].setData(value);
                cacheArray[i].setTimeStamp(getCurrentTime());
                entryUpdated = true;
            }
        }

        if(!entryUpdated){
            CacheEntry<V> newEntry = new CacheEntry<>(intKey, value, false, getCurrentTime());
            if(isEntryValid){
                cacheArray[validIndex] = newEntry;
            }
            else{
                int evictedIndex = replacementStrategy.evictEntryIndex(cacheArray, startIndex, endIndex);
                cacheArray[evictedIndex] = newEntry;
            }
        }
    }

    /**
     * Clear the cache
     */
    public final void clear() {
        for (int i = 0; i < cacheArray.length; i++)
            cacheArray[i] = new CacheEntry<V>();
    }

    /**
     * Use MD5 for hashing
     * @param key
     * @return hashCode
     */
    public int getHash(K key) {
        byte[] bytesOfKey = null;
        try {
            bytesOfKey = key.toString().getBytes("UTF-8");
        } catch(UnsupportedEncodingException e) {
            Logger.getLogger(NSetCache.class.getName()).log(Level.SEVERE, null, e);
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(NSetCache.class.getName()).log(Level.SEVERE, null, e);
        }
        byte[] hashBytes = md.digest(bytesOfKey);
        return Math.abs(byteArrayToInt(hashBytes));
    }


    private long getCurrentTime() {
        return System.nanoTime();
    }

    private int getStartIndex(int key) {
        return (key % numSet) * numEntry;
    }

    private int getEndIndex(int startIndex) {
        return startIndex + numEntry - 1;
    }

    private int byteArrayToInt(byte[] hashBytes) {
        final ByteBuffer b = ByteBuffer.wrap(hashBytes);
        b.order(ByteOrder.LITTLE_ENDIAN);
        return b.getInt();
    }

    public final int size() {
        return numSet * numEntry;
    }
}
