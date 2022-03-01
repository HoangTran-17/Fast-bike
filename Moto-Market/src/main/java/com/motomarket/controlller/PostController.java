package com.motomarket.controlller;

import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

    @Autowired
    private IPostService postService;

    @GetMapping("post")
    public ModelAndView toPostPage(){
        ModelAndView modelAndView = new ModelAndView("new-post");

        return modelAndView;
    }

}
