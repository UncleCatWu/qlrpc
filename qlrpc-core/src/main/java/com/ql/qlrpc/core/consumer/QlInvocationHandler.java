package com.ql.qlrpc.core.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ql.qlrpc.core.api.PpcResponse;
import com.ql.qlrpc.core.api.RpcRequest;
import com.ql.qlrpc.core.util.MethodUtils;
import com.ql.qlrpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
public class QlInvocationHandler implements InvocationHandler {

    static MediaType JSONTYPE = MediaType.get("application/json;charset=utf-8");

    Class<?> service;

    public QlInvocationHandler(Class<?> service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        if (MethodUtils.checkLocalMethod(method.getName())) {
            return null;
        }

        RpcRequest request = new RpcRequest();
        request.setService(service.getCanonicalName());
        request.setMethodSign(MethodUtils.methodSign(method));
        request.setArgs(objects);

        PpcResponse response = post(request);

        if (response.isStatus()) {
            Object data = response.getData();
            if (data instanceof JSONObject jsonObject) {
                return jsonObject.toJavaObject(method.getReturnType());
            } else {
                return TypeUtils.cast(data, Long.class);
            }
        } else {
            Exception ex = response.getEx();
            throw new RuntimeException(ex);
        }
    }

    OkHttpClient client = new OkHttpClient.Builder().connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS)).readTimeout(5, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS).build();

    private PpcResponse post(RpcRequest req) {
        String reqJson = JSON.toJSONString(req);
        log.info("===> reqJson:{}", JSON.toJSONString(reqJson));
        Request request = new Request.Builder().url("http://localhost:8080/").post(RequestBody.create(reqJson, JSONTYPE)).build();

        try {
            String respJson = client.newCall(request).execute().body().string();
            log.info("===> respJson:{}", JSON.toJSONString(respJson));
            PpcResponse response = JSON.parseObject(respJson, PpcResponse.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
