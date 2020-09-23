package com.samuraiiway.myredis.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.*;

@RestController
@RequestMapping("/redis")
public class RedisListener {

    @Autowired
    private FluxProcessor processor;

    @RequestMapping(value = "/stream/listen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent> listenStream() {
        return processor.map(e -> ServerSentEvent.builder(e).build());
    }
}
