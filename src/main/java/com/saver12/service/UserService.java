package com.saver12.service;

import com.saver12.dto.UserDTO;
import com.saver12.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(Long userId);

    User saveUser(UserDTO user);

    User updateUser(Long id, UserDTO user);

    User deleteUser(Long id);
}
