package com.motomarket.controlller;

import com.motomarket.repository.model.Image;
import com.motomarket.repository.model.Ownership;
import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class PostController {

    @Value("${server.rootPath}")
    private String rootPath;

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @GetMapping("post")
    public ModelAndView toPostPage(){
        ModelAndView modelAndView = new ModelAndView("new-post");
        modelAndView.addObject("post", new Post());
        return modelAndView;
    }

    @PostMapping("post")
    public ModelAndView handlePost(@ModelAttribute Post post, @RequestParam Long ownershipSelect, @RequestParam("ip-upload-multi") MultipartFile[] files) throws IOException {
        Date date = new Date();
        post.setStatusPost(StatusPost.PUBLIC);
        post.setPostDate(date);
        if (ownershipSelect == 0){
            post.setOwnership(Ownership.OWNERSHIP);
        } else {
            post.setOwnership(Ownership.NO_OWNERSHIP);
        }
        for (MultipartFile file : files) {
            UUID uuidCode = UUID.randomUUID();
            Image image = new Image();
            image.setImageName(uuidCode.toString());
            image.setPosts(post);
            file.transferTo(new File(rootPath + "/" + uuidCode+".png"));
        }
//        post.setUser(userService.getById(1L));
        System.out.println(post);
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

}
