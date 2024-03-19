package com.jacie.qlrpc.demo.provider;

import com.ql.qlrpc.core.api.PpcResponse;
import com.ql.qlrpc.core.api.RpcRequest;
import com.ql.qlrpc.core.provider.ProviderBootstrap;
import com.ql.qlrpc.core.provider.ProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import(ProviderConfig.class)
public class QlrpcDemoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(QlrpcDemoProviderApplication.class, args);
    }

    @Autowired
    ProviderBootstrap providerBootstrap;

    //使用 HTTP + JSON 来实现序列化和通信
    @RequestMapping("/")
    public PpcResponse<?> invoke(@RequestBody RpcRequest request) {
        return providerBootstrap.invoke(request);
    }

    @Bean
    ApplicationRunner providerRun() {
        return x -> {
            RpcRequest request = new RpcRequest();
            request.setService("com.jacie.qlrpc.demo.api.UserService");
            request.setMethodSign("findById@1_int");
            request.setArgs(new Object[]{100});

            PpcResponse<?> response = providerBootstrap.invoke(request);
            System.out.println("return:" + response.getData());
        };
    }

}
