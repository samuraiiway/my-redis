package com.samuraiiway.myredis.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findUserByName(String name);

    List<UserEntity> findUserByRole(String role);
}
