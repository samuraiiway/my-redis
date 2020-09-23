package com.samuraiiway.myredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

@Configuration
public class StreamConfiguration {

    @Bean
    public FluxProcessor fluxProcessor() {
        return DirectProcessor.create().serialize();
    }

    @Bean
    public FluxSink fluxSink() {
        return fluxProcessor().sink();
    }
}
