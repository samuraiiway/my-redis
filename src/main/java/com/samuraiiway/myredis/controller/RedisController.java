package com.samuraiiway.myredis.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samuraiiway.myredis.model.User;
import com.samuraiiway.myredis.repository.Expire;
import com.samuraiiway.myredis.repository.Repository;
import com.samuraiiway.myredis.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("redis")
public class RedisController {

    static final ObjectMapper mapper = new ObjectMapper();
    static final String ID = "id";
    static final String INDEXES = "indexes";
    static final String EXPIRED = "expired";

    @Autowired
    private Repository repository;

    @Autowired
    private Expire expire;

    @PostMapping("/{namespace}")
    public ResponseEntity upsertData(@PathVariable String namespace, @RequestBody String jsonString) throws Exception {
        // Parse request body to json tree
        JsonNode jsonNode = mapper.readTree(jsonString);
        ObjectNode objectNode = (ObjectNode) jsonNode;

        String id;

        // Find id or auto generate
        JsonNode idNode = jsonNode.get(ID);
        if (idNode == null || idNode.isNull()) {
            id = UUID.randomUUID().toString();
            objectNode.put(ID, id);
        } else {
            id = jsonNode.get(ID).asText();
        }

        // Find indexes and remove
        List<String> indexesName = new ArrayList<>();
        JsonNode indexesNode = jsonNode.get(INDEXES);

        if (indexesNode != null && !indexesNode.isNull() && indexesNode.isArray()) {
            for (JsonNode index: indexesNode) {
                indexesName.add(index.asText());
            }
            objectNode.remove(INDEXES);
        }

        // Find index values
        List<String> keys = new ArrayList<>();

        for (String index : indexesName) {
            JsonNode valueNode = jsonNode.get(index);
            if (valueNode != null && !valueNode.isNull()) {
                keys.add(index + ":" + valueNode.asText());
            }
        }

        // Find expire
        JsonNode expiredNode = jsonNode.get(EXPIRED);

        if (expiredNode != null && !expiredNode.isNull() && expiredNode.isInt()) {
            Integer expired = jsonNode.get(EXPIRED).asInt();
            expire.setExpire(namespace, id, expired);
        }

        // Save indexes
        repository.saveIndex(namespace, id, keys);

        // Save data
        repository.saveData(namespace, id, mapper.writeValueAsString(objectNode));


        return ResponseEntity.ok(objectNode);
    }

    @RequestMapping(value = "/{namespace}/{index}/{value}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getData (
            @PathVariable String namespace,
            @PathVariable String index,
            @PathVariable String value
    ) {
        if (ID.equals(index)) {
            return ResponseEntity.ok(repository.getById(namespace, value));
        } else {
            List<String> data = repository.getByIndex(namespace, index + ":" + value);
            String result = "[" + String.join(",", data) + "]";
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/{namespace}/generate/{number}")
    public ResponseEntity generateData(@PathVariable String namespace, @PathVariable int number) throws Exception {

        for (int i = 0; i < number; i++) {
            String id = UUID.randomUUID().toString();
            String name = Random.getRandomString(2);
            String password = Random.getRandomString(40);
            String role = Random.getRandomRole(i);

            User user = new User(id, name, password, role);
            List<String> keys = Arrays.asList("name:" + name, "password:" + password, "role:" + role);
            repository.saveIndex(namespace, id, keys);
            repository.saveData(namespace, id, mapper.writeValueAsString(user));
        }

        return ResponseEntity.ok("");
    }
}
