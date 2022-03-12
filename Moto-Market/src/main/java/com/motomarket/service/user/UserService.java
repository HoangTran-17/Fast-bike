package com.motomarket.service.user;

import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.dto.UserView;
import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.time.Instant.*;

@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPostService postService;

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

    @Override
    public UserView getUserViewById(Long id) {
        User user = userRepository.getById(id);
        UserView userView = UserView.parseUserView(user);

        String timePeriod = calculateTheElapsedTime(userView.getCreated());
        userView.setTimePeriod(timePeriod);
        int countPublicPost = postService.getCountPublicPostByUser(user);
        userView.setCountPublicPost(countPublicPost);
        int countSoldPost = postService.getCountSoldPostByUser(user);
        userView.setCountSoldPost(countSoldPost);
        return userView;
    }

    private String calculateTheElapsedTime(Date created) {
        Long untilNow = new Date().getTime() - created.getTime();
        
        Long SECOND = 1000L;
        Long MINUTE = 60 * SECOND;
        Long HOUR = 60 * MINUTE;
        Long DAY = 24 * HOUR;
        Long WEEK = 7 * DAY;
        Long MONTH = 30 * DAY;
        Long YEAR = 365 * DAY;
        Long[] list1 = {YEAR, MONTH, WEEK, DAY, HOUR, MINUTE};
        String[] list2 = {"năm", "tháng", "tuần", "ngày", "giờ", "phút"};

        int count = 0;
        String timePeriod = "";
        for (int i = 0; count == 0; ++i) {
            count = Math.round(untilNow / list1[i]) ;
            if (count > 0) {
                timePeriod = count + " " + list2[i];
                break;
            }
        }
        return timePeriod;
    }


    private User parseUser(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getAvatar(), userDTO.getUserName(), userDTO.getEmail(),
                userDTO.getRole(), userDTO.getUserStatus(),userDTO.getCreated(), userDTO.getPassword()
               ,userDTO.isDeleted(), userDTO.getPhoneNumber(), null);
    }
}
