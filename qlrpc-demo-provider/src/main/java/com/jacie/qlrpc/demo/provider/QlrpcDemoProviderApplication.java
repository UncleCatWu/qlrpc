package com.jacie.qlrpc.demo.provider;

import com.ql.qlrpc.core.api.PpcResponse;
import com.ql.qlrpc.core.api.RpcRequest;
import com.ql.qlrpc.core.provider.ProviderConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    //使用 HTTP + JSON 来实现序列化和通信
    @RequestMapping("/")
    public PpcResponse<?> invoke(@RequestBody RpcRequest request) {
        return invokeRequest(request);
    }


}
