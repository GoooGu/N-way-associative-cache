import static org.junit.Assert.*;

import org.junit.Test;

public class ClientCacheTest {
    @Test
    public void testDirMapCache() {
        ClientCache<String, String> cache = new ClientCache<>(2,1, new LRUReplacement<String>());
        cache.put("Trade", "Desk");
        assertEquals("Desk", cache.get("Trade"));
        cache.put("UCSD", "San Diego");
        assertEquals("Desk", cache.get("Trade"));
        assertEquals("San Diego", cache.get("UCSD"));
    }

    @Test
    public void testLRU() {
        ClientCache<Integer, String> cache = new ClientCache<>(8, 2, new LRUReplacement<String>());
        cache.put(16, "TTD");
        cache.put(32, "UCSD");
        assertEquals("TTD",cache.get(16));
        cache.put(64, "JAVA");
        assertEquals("TTD", cache.get(16));
        assertEquals("JAVA", cache.get(64));
    }

    @Test
    public void testMRU() {
        ClientCache<Integer, String> cache = new ClientCache<>(8, 2, new MRUReplacement<String>());
        cache.put(16, "TTD");
        cache.put(32, "UCSD");
        assertEquals("TTD",cache.get(16));
        cache.put(64, "JAVA");
        assertEquals(null, cache.get(16));
        assertEquals("JAVA", cache.get(64));
    }

    @Test
    public void testUpdateCache() {
        ClientCache<Integer, String> cache = new ClientCache<>(8, 2, new LRUReplacement<String>());
        cache.put(16, "TTD");
        cache.put(16, "UCSD");
        assertEquals("UCSD", cache.get(16));
    }

    @Test
    public void testCustomReplacement() {
        ClientCache<Integer, String> cache = new ClientCache<>(8, 4, new CustomReplacement<String>());
        cache.put(16, "UCSD");
        cache.put(32, "TTD");
        cache.put(48, "JAVA");
        cache.put(64, "INTELLIJ");
        cache.put(80, "ADS");
        assertEquals("UCSD", cache.get(16));
        assertEquals("TTD", cache.get(32));

        // "Java" should be kicked out because it's the second most recently used
        assertEquals(null, cache.get(48));

        assertEquals("INTELLIJ", cache.get(64));

        //"ADS" should take the spot "Java" had
        assertEquals("ADS", cache.get(80));
    }
}
