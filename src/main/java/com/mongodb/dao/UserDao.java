package com.mongodb.dao;

import com.mongodb.model.User;

public interface UserDao {

    void saveUser(User user);

    User findUserByUserName(String userName);

    long updateUser(User user);

    void deleteUserById(Long id);

}
