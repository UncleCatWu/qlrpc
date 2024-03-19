package com.ql.qlrpc.core.provider;

import com.ql.qlrpc.core.annotation.QlProvider;
import com.ql.qlrpc.core.api.PpcResponse;
import com.ql.qlrpc.core.api.RpcRequest;
import com.ql.qlrpc.core.meta.ProviderMeta;
import com.ql.qlrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    @PostConstruct
    public void start() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(QlProvider.class);
        providers.forEach((k, v) -> System.out.println(k));
        //skeleton.putAll(providers);
        providers.values().forEach(this::genInterface);
    }

    private void genInterface(Object x) {
        Class<?> anInterface = x.getClass().getInterfaces()[0];
        Method[] methods = anInterface.getMethods();
        for (Method method : methods) {
            if (MethodUtils.checkLocalMethod(method)) {
                continue;
            }
            createProvider(anInterface, x, method);
        }
    }

    private void createProvider(Class<?> anInterface, Object x, Method method) {
        ProviderMeta meta = new ProviderMeta();
        meta.setMethod(method);
        meta.setMethodSign(MethodUtils.methodSign(method));
        meta.setServiceImpl(x);
        System.out.println("create a provider:" + meta);
        skeleton.add(anInterface.getCanonicalName(), meta);
    }

    public PpcResponse<?> invoke(RpcRequest request) {

        PpcResponse rpcResponse = new PpcResponse<>();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());

            Method method = meta.getMethod();
            Object result = method.invoke(meta.getServiceImpl(), request.getArgs());
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
            return rpcResponse;
        } catch (InvocationTargetException e) {
            rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        } catch (IllegalAccessException e) {
            rpcResponse.setEx(new RuntimeException(e.getMessage()));
        }
        return rpcResponse;
    }

    private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
        Optional<ProviderMeta> first = providerMetas.stream().filter(x -> x.getMethodSign().equals(methodSign)).findFirst();
        return first.orElse(null);
    }
}
