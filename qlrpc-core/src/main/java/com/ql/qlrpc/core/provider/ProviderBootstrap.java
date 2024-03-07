package com.ql.qlrpc.core.provider;

import com.ql.qlrpc.core.annotation.QlProvider;
import com.ql.qlrpc.core.api.PpcResponse;
import com.ql.qlrpc.core.api.RpcRequest;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private Map<String, Object> skeleton = new HashMap<>();

    @PostConstruct
    public void buildProviders() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(QlProvider.class);
        providers.forEach((k, v) -> System.out.println(k));

        //skeleton.putAll(providers);
        providers.values().forEach(this::genInterface);
    }

    private void genInterface(Object x) {
        Class<?> anInterface = x.getClass().getInterfaces()[0];
        skeleton.put(anInterface.getCanonicalName(), x);
    }

    public PpcResponse<?> invoke(RpcRequest request) {

        Object bean = skeleton.get(request.getService());
        try {
            Method method = findMethod(bean.getClass(), request.getMethod());
            Object result = method.invoke(bean, request.getArgs());
            return new PpcResponse<>(true, result);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Method findMethod(Class<?> aClass, String method) {
        for (Method aClassMethod : aClass.getMethods()) {
            if (aClassMethod.getName().equals(method)) {
                return aClassMethod;
            }
        }
        return null;
    }
}
