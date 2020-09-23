package com.samuraiiway.myredis.controller;

import com.samuraiiway.myredis.model.UserEntity;
import com.samuraiiway.myredis.model.UserRepository;
import com.samuraiiway.myredis.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sql")
public class SqlController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/name/{name}")
    public ResponseEntity getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userRepository.findUserByName(name));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity getUserByRole(@PathVariable String role) {
        return ResponseEntity.ok(userRepository.findUserByRole(role));
    }

    @PostMapping("/generate/{number}")
    public ResponseEntity generateMySQLData(@PathVariable int number) {

        for (int i = 0; i < number; i++) {
            String name = Random.getRandomString(2);
            String password = Random.getRandomString(40);
            String role = Random.getRandomRole(i);

            UserEntity user = new UserEntity();
            user.setName(name);
            user.setPassword(password);
            user.setRole(role);

            userRepository.save(user);
        }

        return ResponseEntity.ok("");
    }

    @PostMapping("/generate/file/{number}")
    public ResponseEntity generateMySQLFileData(@PathVariable int number) {

        StringBuilder str = new StringBuilder();
        str.append("INSERT INTO user.user(name, password, role) VALUES\n");

        for (int i = 0; i < number; i++) {
            String name = Random.getRandomString(2);
            String password = Random.getRandomString(40);
            String role = Random.getRandomRole(i);

            str.append("('");
            str.append(name);
            str.append("', '");
            str.append(password);
            str.append("', '");
            str.append(role);
            str.append("'),\n");
        }

        return ResponseEntity.ok(str.toString());
    }
}
