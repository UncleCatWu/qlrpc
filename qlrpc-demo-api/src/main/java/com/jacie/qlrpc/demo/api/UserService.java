package com.jacie.qlrpc.demo.api;

public interface UserService {
    User findById(int id);

    User findById(int id, String name);

    Long getId(long id);

    Long getId(User user);
}
