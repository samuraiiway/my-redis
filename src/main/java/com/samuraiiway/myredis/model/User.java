package com.samuraiiway.myredis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String id;

    private String name;

    private String password;

    private String role;
}
