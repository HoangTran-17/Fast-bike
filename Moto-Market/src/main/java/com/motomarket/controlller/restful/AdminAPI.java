package com.motomarket.controlller.restful;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.response.PostResponse;
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
    @Autowired
    private IPostService postService;

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

    @GetMapping("/all-post")
    public PostResponse getAllPostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                                               @RequestParam(defaultValue = "5") Integer pageSize)
    {
       return postService.findPostDeletedIsFalseOrderByDate(pageNo,pageSize);

    }
}
