package com.motomarket.controlller.restful;

import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.response.PostResponse;
import com.motomarket.service.response.UserResponse;
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
    public ResponseEntity<UserDTO> deleteUserByAdmin(@RequestBody Long userId) {

            User user = userService.getUserById(userId);
            user.setDeleted(true);
            User saveUser = userService.save(user);
            UserDTO userDTO =UserDTO.parseUserDTO(saveUser);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
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
    @GetMapping("/waiting-post")
    public PostResponse getWaitingPostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return postService.findPostWaitingOrderByDate(pageNo,pageSize);

    }

    @GetMapping("/hide-post")
    public PostResponse getHidePostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                                         @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return postService.findPostHideOrderByDate(pageNo,pageSize);

    }

    @GetMapping("/post/search/{key}")
    public PostResponse getAllPostByKeySearch(@PathVariable String key,@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
        return postService.findPostByKeySearch(key, pageNo, pageSize);
    }

    @GetMapping("/waiting-post/search/{key}")
    public PostResponse getWaitingPostByKeySearch(@PathVariable String key,@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
        return postService.findWaitingPostByKeySearch(key, pageNo, pageSize);
    }

    @GetMapping("/hide-post/search/{key}")
    public PostResponse getHidePostByKeySearch(@PathVariable String key,@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "5") Integer pageSize) {
        return postService.findHidePostByKeySearch(key, pageNo, pageSize);
    }

    @DeleteMapping("/post/delete/{postId}")
    public PostDTO deletePost(@PathVariable Long postId) {
        postService.remove(postId);
        return postService.getById(postId);
    }

    @PutMapping ("/post/hide/{postId}")
    public PostDTO hidePost(@PathVariable Long postId) {
        postService.hide(postId);
        return postService.getById(postId);
    }

    @PutMapping ("/post/public/{postId}")
    public PostDTO publicPost(@PathVariable Long postId) {
        postService.publicPost(postId);
        return postService.getById(postId);
    }

    @GetMapping("/api/users/dba")
    public UserResponse getAllUserByDBA(@RequestParam(defaultValue = "0") Integer pageNo,
                                   @RequestParam(defaultValue = "5") Integer pageSize)
    {
            return userService.findAllUserByDeletedIsFalseByDBA(pageNo,pageSize);
    }

    @GetMapping("/api/users")
    public UserResponse getAllUserByAdmin(@RequestParam(defaultValue = "0") Integer pageNo,
                                        @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return userService.findAllUserByDeletedIsFalseByAdmin(pageNo,pageSize);
    }

    @GetMapping("/users/search/{key}")
    public UserResponse getAllUserByKeySearch(@PathVariable String key ,@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
            return userService.findUserByKeySearchByAdmin(key,pageNo,pageSize);
    }

    @GetMapping("/users/search/dba/{key}")
    public UserResponse getAllUserByKeySearchByDBA(@PathVariable String key ,@RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
        return userService.findUserByKeySearchByDBA(key,pageNo,pageSize);
    }
}
