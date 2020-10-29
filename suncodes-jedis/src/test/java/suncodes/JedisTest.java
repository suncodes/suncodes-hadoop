package suncodes;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisTest {

    @Test
    public void f() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        jedis.close();
    }

    @Test
    public void fList() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);

        jedis.lpush("list1", "a", "b", "c");
        jedis.rpush("list1", "x");

        List<String> list1 = jedis.lrange("list1", 0, -1);
        for (String s : list1) {
            System.out.println(s);
        }
        jedis.close();
    }

    @Test
    public void fHash() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);

        jedis.hset("hash1", "a1", "a1");
        jedis.hset("hash1", "a2", "a2");
        jedis.hset("hash1", "a3", "a3");

        Map<String, String> map = jedis.hgetAll("hash1");
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            System.out.println(stringStringEntry.getKey());
            System.out.println(stringStringEntry.getValue());
        }
        jedis.close();
    }

}
