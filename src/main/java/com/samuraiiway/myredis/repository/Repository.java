package com.samuraiiway.myredis.repository;

import com.samuraiiway.myredis.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.*;

@Service
public class Repository {

    static private final Map<String, Map<String, String>> rootNamespace = new HashMap<>();

    static private final Map<String, Map<String, Set<String>>> rootIndex = new HashMap<>();

    @Autowired
    private FluxSink sink;

    synchronized private Map<String, String> getNameSpace(String namespace) {
        Map<String, String> map = rootNamespace.get(namespace);
        if (map == null) {
            map = new HashMap<>();
            rootNamespace.put(namespace, map);
        }

        return map;
    }

    synchronized private Set<String> getIndexKey(String namespace, String key) {
        Map<String, Set<String>> map = rootIndex.get(namespace);

        if (map == null) {
            map = new HashMap<>();
            rootIndex.put(namespace, map);
        }

        Set<String> set = map.get(key);
        if (set == null) {
            set = new HashSet<>();
            map.put(key, set);
        }

        return set;
    }

    synchronized void updateNamespace(Action action, Map<String, String> map, String id, String data) {
        if (action == Action.SAVE) {
            map.put(id, data);
        } else if(action == Action.DELETE) {
            map.remove(id);
        }
    }

    public void saveData(String namespace, String id, String data) {
        Map<String, String> map = getNameSpace(namespace);

        updateNamespace(Action.SAVE, map, id, data);
        sink.next(Action.SAVE.toString() + ":" + namespace + ":" + id);
    }

    synchronized public void saveIndex(String namespace, String id, List<String> keys) {
        for (String key : keys) {
            Set<String> index = getIndexKey(namespace, key);
            index.add(id);
        }
    }

    public String getById(String namespace, String id) {
        Map<String, String> map = getNameSpace(namespace);
        return map.get(id);
    }

    public List<String> getByIndex(String namespace, String key) {
        List<String> result = new ArrayList<>();
        Set<String> ids = getIndexKey(namespace, key);

        for (String id : ids) {
            String data =  getById(namespace, id);
            if (data != null) {
                result.add(data);
            }
        }

        return result;
    }

    public void deleteById(String namespace, String id) {
        Map<String, String> map = getNameSpace(namespace);
        updateNamespace(Action.DELETE, map, id, null);
        sink.next(Action.DELETE.toString() + ":" + namespace + ":" + id);
    }
}
