package com.samuraiiway.myredis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Expire {
    static final Map<String, Integer> expireSet = new HashMap<>();
    static final SortedMap<Integer, List<String>> expireQueue = new TreeMap<>();

    @Autowired
    private Repository repository;

    synchronized public void setExpire(String namespace, String id, int seconds) {
        String key = namespace + ":" + id;
        Integer time = (int)(System.currentTimeMillis() / 1000) + seconds;

        expireSet.put(key, time);

        List<String> keys = expireQueue.get(time);
        if (keys == null) {
            keys = new ArrayList<>();
            expireQueue.put(time, keys);
        }

        keys.add(key);
    }

    @Scheduled(fixedRate = 1000)
    private void expiredScheduler() {
        Integer now = (int)(System.currentTimeMillis() / 1000);

        Iterator<Map.Entry<Integer, List<String>>> iterator = expireQueue.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, List<String>> entry = iterator.next();
            Integer key = entry.getKey();

            if (key > now) {
                break;
            }

            List<String> ids = entry.getValue();
            for (String id : ids) {
                if (key.equals(expireSet.get(id))) {
                    String[] name = id.split(":");
                    repository.deleteById(name[0], name[1]);
                }
            }

            iterator.remove();
        }
    }

}
