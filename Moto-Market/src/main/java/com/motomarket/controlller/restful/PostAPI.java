package com.motomarket.controlller.restful;

import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
