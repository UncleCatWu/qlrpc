package com.ql.qlrpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PpcResponse<T> {

    private boolean status; //状态： true

    private T data; //new User

    Exception ex;
}
