package com.motomarket.controlller.restful;

import com.motomarket.repository.model.StatusPost;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostAPI {
    @Autowired
    private IPostService postService;

    @GetMapping("/getListOfLatestPosts")
    public ResponseEntity<List<PostDTO>> GetListOfLatestPosts() {
        List<PostDTO> postDTOs = postService.findListOfLatestPosts(18);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{statusPost}")
    public PostResponse getListPostByUserIdAndStatusPost(@PathVariable Long userId, @PathVariable StatusPost statusPost,@RequestParam(defaultValue = "0") Integer pageNo,
                                                         @RequestParam(defaultValue = "10") Integer pageSize) {
      return  postService.findAllByUserIdAndStatusPost(userId, statusPost, pageNo, pageSize);

    }
}
