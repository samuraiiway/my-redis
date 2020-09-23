package com.samuraiiway.myredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyRedisApplication.class, args);
	}

}
