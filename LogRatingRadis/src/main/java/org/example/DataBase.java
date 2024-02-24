package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

public class DataBase {
    private JedisPool pool = new JedisPool("localhost", 6379);
    public void insertInBD(long chatId, String login, String password)  {
        try (Jedis jedis = pool.getResource()) {
            jedis.hset(String.valueOf(chatId), login, password);
        }
    }

    public boolean CheckChatId(long chatId)  {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> resultSet = jedis.hgetAll(String.valueOf(chatId));
            if (resultSet.isEmpty()){
                return false;
            }
        }
        return true;
    }

    public String outputLogin(long chatId) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hkeys(String.valueOf(chatId)).stream().toList().getFirst();
        }
    }

    public String outputPassword(long chatId)  {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hvals(String.valueOf(chatId)).getFirst();
        }
    }
    public void deleteData(long chatId) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(String.valueOf(chatId));
        }
    }
}