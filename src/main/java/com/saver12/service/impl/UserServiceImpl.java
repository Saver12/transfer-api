package com.saver12.service.impl;

import com.saver12.db.DBStore;
import com.saver12.dto.UserDTO;
import com.saver12.exception.AppException;
import com.saver12.model.User;
import com.saver12.service.UserService;

import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class UserServiceImpl implements UserService {

    private static final AtomicLong GENERATED_ID = new AtomicLong(4);
    public static final String EMPTY_REQUEST_BODY = "empty request body";
    public static final String USER_NOT_FOUND = "user not found";
    public static final String MISSING_VALUES = "JSON should contain name and email values";

    @Override
    public List<User> getAllUsers() {
        return DBStore.getAllUsers();
    }

    public User getUser(Long userId) {
        User user = DBStore.getUser(userId);
        if (user == null) {
            throw new AppException(USER_NOT_FOUND);
        }
        return user;
    }

    public User saveUser(UserDTO user) {
        checkRequestBody(user);
        User saved = new User(GENERATED_ID.getAndIncrement(), user.getName(), user.getEmail());
        DBStore.addUser(saved);
        return saved;
    }

    public User updateUser(Long id, UserDTO user) {
        checkRequestBody(user);
        User updated = DBStore.updateUser(id, user);
        if (updated == null) {
            throw new AppException(USER_NOT_FOUND);
        }
        return updated;
    }

    @Override
    public User deleteUser(Long id) {
        User deleted = DBStore.deleteUser(id);
        if (deleted == null) {
            throw new AppException(USER_NOT_FOUND);
        }
        return deleted;
    }

    private void checkRequestBody(UserDTO user) {
        if (user == null) {
            throw new AppException(EMPTY_REQUEST_BODY);
        } else if (user.getName() == null || user.getEmail() == null) {
            throw new AppException(MISSING_VALUES);
        }
    }
}
