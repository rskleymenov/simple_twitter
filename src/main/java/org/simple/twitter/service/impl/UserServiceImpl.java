package org.simple.twitter.service.impl;

import lombok.extern.log4j.Log4j;
import org.dozer.Mapper;
import org.simple.twitter.dto.UserDTO;
import org.simple.twitter.exceptions.NoteAppException;
import org.simple.twitter.model.User;
import org.simple.twitter.repository.UserRepository;
import org.simple.twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    @Override
    public List<UserDTO> getAllStoredUsers() {
        List<UserDTO> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(mapUserToDTO(user)));
        return users;
    }

    @Override
    public UserDTO saveUser(UserDTO userDto) {
        if (userDto.getId() != null) {
            throw new NoteAppException("Invalid input data: should not specify id", HttpStatus.BAD_REQUEST);
        }
        userDto.setCreationDate(new Timestamp(new Date().getTime()));
        User savedUser = null;
        try {
            savedUser = userRepository.save(mapUserDTOToUser(userDto));
        } catch (DataAccessException ex) {
            log.error(ex);
            throw new NoteAppException(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return mapUserToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User loadedUser = userRepository.findOne(userDTO.getId());
        List<User> sameUsername = userRepository.findByUsername(userDTO.getUsername());
        if (loadedUser == null) {
            throw new NoteAppException("User is not stored in db", HttpStatus.NOT_FOUND);
        } else if (!sameUsername.isEmpty()) {
            throw new NoteAppException("Same username is already stored to db", HttpStatus.BAD_REQUEST);
        }
        User save = null;
        mapper.map(userDTO, loadedUser);
        try {
            save = userRepository.save(loadedUser);
        } catch (DataAccessException ex) {
            log.error(ex);
            throw new NoteAppException(ex.toString(), HttpStatus.BAD_REQUEST);
        }
        return mapUserToDTO(save);
    }

    @Override
    public UserDTO findUser(int id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new NoteAppException("User not found", HttpStatus.NOT_FOUND);
        }
        return mapUserToDTO(user);
    }

    @Override
    public void deleteUser(int id) {
        try {
            userRepository.delete(id);
        } catch (DataAccessException ex) {
            log.error(ex);
            throw new NoteAppException(ex.toString(), HttpStatus.NOT_FOUND);
        }
    }

    private UserDTO mapUserToDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    private User mapUserDTOToUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }
}
