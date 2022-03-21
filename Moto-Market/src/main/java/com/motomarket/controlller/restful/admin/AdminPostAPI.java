package com.motomarket.controlller.restful.admin;

import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/post")
public class AdminPostAPI {

    @Autowired
    private IPostService postService;

    @GetMapping("/all")
    public PostResponse getAllPostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "5") Integer pageSize)
    {
        return postService.findPostDeletedIsFalseOrderByDate(pageNo,pageSize);

    }
    @GetMapping("/waiting")
    public PostResponse getWaitingPostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "5") Integer pageSize)
    {
       StatusPost statusPost = StatusPost.WAITING;
        return postService.findPostByStatusPostOrderByDate(statusPost,pageNo,pageSize);

    }

    @GetMapping("/hide")
    public PostResponse getHidePostWithPageable(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "5") Integer pageSize)
    {
        StatusPost statusPost = StatusPost.HIDE;
        return postService.findPostByStatusPostOrderByDate(statusPost,pageNo,pageSize);

    }

    @GetMapping("/search/{key}")
    public PostResponse getAllPostByKeySearch(@PathVariable String key, @RequestParam(defaultValue = "0") Integer pageNo,
                                              @RequestParam(defaultValue = "5") Integer pageSize) {
        return postService.findPostByKeySearch(key, pageNo, pageSize);
    }

    @GetMapping("/waiting/search/{key}")
    public PostResponse getWaitingPostByKeySearch(@PathVariable String key,@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "5") Integer pageSize) {
       StatusPost statusPost = StatusPost.WAITING;
        return postService.findPostByStatusPostByKeySearch(key,statusPost, pageNo, pageSize);
    }

    @GetMapping("/hide/search/{key}")
    public PostResponse getHidePostByKeySearch(@PathVariable String key,@RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "5") Integer pageSize) {
        StatusPost statusPost = StatusPost.HIDE;
        return postService.findPostByStatusPostByKeySearch(key,statusPost, pageNo, pageSize);
    }

    @DeleteMapping("/delete/{postId}")
    public PostDTO deletePost(@PathVariable Long postId) {
        StatusPost statusPost = StatusPost.DELETE;
        postService.setStatusPostById(statusPost,postId);
        return postService.getById(postId);
    }

    @PutMapping ("/hide/{postId}")
    public PostDTO hidePost(@PathVariable Long postId) {
        StatusPost statusPost = StatusPost.HIDE;
        postService.setStatusPostById(statusPost,postId);
        return postService.getById(postId);
    }

    @PutMapping ("/public/{postId}")
    public PostDTO publicPost(@PathVariable Long postId) {
        UserDTO user = postService.getById(postId).getUserDTO();
        if (user.getUserStatus().equals(StatusUser.BLOCK)) {
            throw new RuntimeException();
        }
        StatusPost statusPost = StatusPost.PUBLIC;
        postService.setStatusPostById(statusPost,postId);
        return postService.getById(postId);
    }

}
