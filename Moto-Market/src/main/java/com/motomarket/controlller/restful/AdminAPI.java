package com.motomarket.controlller.restful;

import com.motomarket.repository.model.StatusUser;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminAPI {
    @Autowired
    private IUserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<List<UserDTO>> deleteUserByAdmin(@RequestBody List<Long> listId) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (Long id : listId
        ) {
            User user = userService.getUserById(id);
            user.setDeleted(true);
            User saveUser = userService.save(user);
            userDTOS.add(UserDTO.parseUserDTO(saveUser));
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @PutMapping("/edit-status/{id}")
    public ResponseEntity<UserDTO> changeStatusUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user.getUserStatus().equals(StatusUser.BLOCK)) {
            user.setUserStatus(StatusUser.ACTIVATE);
        } else {
            user.setUserStatus(StatusUser.BLOCK);
        }
        userService.save(user);
        return new ResponseEntity<>(UserDTO.parseUserDTO(user), HttpStatus.OK);
    }
}
