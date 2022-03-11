package com.motomarket.service.user;

import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> userDTOList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            userDTOList.add(UserDTO.parseUserDTO(user));
        });
        return userDTOList;
    }

    @Override
    public UserDTO getById(Long id) {
        User user = userRepository.getById(id);
        return UserDTO.parseUserDTO(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User newUser = userRepository.save(parseUser(userDTO));
        return UserDTO.parseUserDTO(newUser);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public UserDTO getByUserName(String userName){
        User user = userRepository.findUserByUserName(userName);
        if (user == null) {
            return null;
        }
        return UserDTO.parseUserDTO(user);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        return UserDTO.parseUserDTO(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAllByDeletedIsFalse() {
        List<UserDTO> userDTOList = new ArrayList<>();
        userRepository.findAllByDeletedIsFalse().forEach(user -> {
            userDTOList.add(UserDTO.parseUserDTO(user));
        });
        return userDTOList;
    }


    private User parseUser(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getAvatar(), userDTO.getUserName(), userDTO.getEmail(),
                userDTO.getRole(), userDTO.getUserStatus(),userDTO.getCreated(), userDTO.getPassword()
               ,userDTO.isDeleted(), userDTO.getPhoneNumber(), null);
    }
}
