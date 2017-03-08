package org.simple.twitter.service;

import org.simple.twitter.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllStoredUsers();

    UserDTO saveUser(UserDTO user);

    UserDTO updateUser(UserDTO user);

    UserDTO findUser(int id);

    void deleteUser(int id);

}
