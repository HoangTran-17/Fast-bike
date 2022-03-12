package com.motomarket.service.user;

import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getEmail(),
                userDTO.getRole(), userDTO.getUserStatus(), userDTO.getPassword()
               ,userDTO.isDeleted(), userDTO.getPhoneNumber(), null);
    }

    @Override
    public UserResponse findUserByKeySearchByDBA(String key, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> users = userRepository.findUserByKeySearchByDBA(key,pageable);
        return getUserResponse(users);
    }

    @Override
    public UserResponse findUserByKeySearchByAdmin(String key, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> users = userRepository.findUserByKeySearchByAdmin(key,pageable);
        return getUserResponse(users);
    }

    @Override
    public UserResponse findAllUserByDeletedIsFalseByDBA(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> users = userRepository.findAllUserByDeletedIsFalseByDBA(pageable);
        return getUserResponse(users);
    }



    @Override
    public UserResponse findAllUserByDeletedIsFalseByAdmin(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> users = userRepository.findAllUserByDeletedIsFalseByAdmin(pageable);
        return getUserResponse(users);
    }

    private UserResponse getUserResponse(Page<User> users) {
        List<User> userList = users.getContent();
        List<UserDTO> content = userList.stream().map(UserDTO::parseUserDTO).collect(Collectors.toList());
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());
        return userResponse;
    }

}
