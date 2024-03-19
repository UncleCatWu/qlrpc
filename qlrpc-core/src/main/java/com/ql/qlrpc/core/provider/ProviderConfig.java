package com.ql.qlrpc.core.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ProviderConfig {

    @Bean
    ProviderBootstrap providerBootstrap() {
        log.info("providerBootstrap bean begin to create.");
        return new ProviderBootstrap();
    }
}
