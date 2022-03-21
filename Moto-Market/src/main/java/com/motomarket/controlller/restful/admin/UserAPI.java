package com.motomarket.controlller.restful.admin;

import com.motomarket.repository.model.*;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.response.UserDT0Errors;
import com.motomarket.service.response.UserResponse;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/api/user")
public class UserAPI {

    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;

    @PostMapping("/add-new-admin")
    public ResponseEntity<UserDT0Errors> addNewAdmin(@RequestBody @Validated UserDTO userDTO, BindingResult bindingResult) {
        UserDT0Errors userDT0Errors = new UserDT0Errors(userDTO, new ArrayList<>());
        if (bindingResult.hasFieldErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> userDT0Errors.getAllErrors().add(objectError.getDefaultMessage()));
            return new ResponseEntity<>(userDT0Errors, HttpStatus.NOT_FOUND);
        }
        if (userService.findUserByEmail(userDTO.getEmail()) != null) {
            String error = "Email already exist!";
            userDT0Errors.getAllErrors().add(error);
            return new ResponseEntity<>(userDT0Errors, HttpStatus.NOT_FOUND);
        }
        userDTO.setRole(Role.ADMIN);
        userDTO.setCreated(new Date());
        userDTO.setUserStatus(StatusUser.ACTIVATE);
        UserDTO save = userService.save(userDTO);
        userDT0Errors.setUserDTO(save);
        return new ResponseEntity<>(userDT0Errors, HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<UserDTO> deleteUserByAdmin(@RequestBody Long userId) {

        User user = userService.getUserById(userId);
        user.setUserStatus(StatusUser.SUSPENDED);
        user.getPostList().forEach(post -> postService.remove(post.getPostId()));
        User saveUser = userService.save(user);
        UserDTO userDTO =UserDTO.parseUserDTO(saveUser);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/edit-status/{id}")
    public ResponseEntity<UserDTO> changeStatusUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);

        if (user.getUserStatus().equals(StatusUser.BLOCK)) {
            user.setUserStatus(StatusUser.ACTIVATE);
            List<Post> posts = user.getPostList();
            for (Post p: posts
            ) {
                if (p.getStatusPost().equals(StatusPost.BLOCKED)) {
                    StatusPost statusPost = StatusPost.PUBLIC;
                    postService.setStatusPostById(statusPost,p.getPostId());
                }
            }
        } else {
            user.setUserStatus(StatusUser.BLOCK);
            List<Post> posts = user.getPostList();
            for (Post p: posts
            ) {
                if (p.getStatusPost().equals(StatusPost.PUBLIC)) {
                    StatusPost statusPost = StatusPost.BLOCKED;
                    postService.setStatusPostById(statusPost,p.getPostId());
                }
            }
        }
        userService.save(user);
        return new ResponseEntity<>(UserDTO.parseUserDTO(user), HttpStatus.OK);
    }

    @GetMapping("/all/dba")
    public UserResponse getAllUserByDBA(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return userService.findAllUserByDeletedIsFalseByDBA(pageNo,pageSize);
    }

    @GetMapping("/all")
    public UserResponse getAllUserByAdmin(@RequestParam(defaultValue = "0") Integer pageNo,
                                          @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return userService.findAllUserByDeletedIsFalseByAdmin(pageNo,pageSize);
    }

    @GetMapping("/search/{key}")
    public UserResponse getAllUserByKeySearch(@PathVariable String key ,@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.findUserByKeySearchByAdmin(key,pageNo,pageSize);
    }

    @GetMapping("/search/dba/{key}")
    public UserResponse getAllUserByKeySearchByDBA(@PathVariable String key ,@RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.findUserByKeySearchByDBA(key,pageNo,pageSize);
    }

}
