package com.ql.qlrpc.core.api;

import lombok.Data;

@Data
public class RpcRequest {

    private String service; //接口
    private String methodSign; //方法：findById
    private Object[] args; //参数：100
}
