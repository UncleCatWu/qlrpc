package com.ql.qlrpc.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
public class ConsumerConfig {

    @Bean
    ConsumerBoostrap createConsumerBootstrap() {
        log.info("createConsumerBootstrap bean begin to created.");
        return new ConsumerBoostrap();
    }

    @Bean
    @Order(1)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBoostrap consumerBoostrap) {
        return x -> {
            consumerBoostrap.start();
        };
    }
}
