package com.jacie.qlrpc.demo.provider;

import com.jacie.qlrpc.demo.api.User;
import com.jacie.qlrpc.demo.api.UserService;
import com.ql.qlrpc.core.annotation.QlProvider;
import org.springframework.stereotype.Component;

@Component
@QlProvider
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "ql" + System.currentTimeMillis());
    }

    @Override
    public User findById(int id, String name) {
        return new User(id, "ql-" + name + "_" + System.currentTimeMillis());
    }

    @Override
    public Long getId(long id) {
        return id;
    }

    @Override
    public Long getId(User user) {
        return user.getId().longValue();
    }
}
