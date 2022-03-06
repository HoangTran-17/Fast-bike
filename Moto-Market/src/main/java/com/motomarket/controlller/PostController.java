package com.motomarket.controlller;

import com.motomarket.repository.model.*;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.motor.IDetailMotorService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDetailMotorService detailMotorService;

    @GetMapping("post")
    public ModelAndView toPostPage() {
        ModelAndView modelAndView = new ModelAndView("new-post");
        modelAndView.addObject("post", new PostDTO());
        return modelAndView;
    }

    @PostMapping("post")
    public ModelAndView handlePost(@ModelAttribute PostDTO post, @RequestParam Long ownershipSelect, @RequestParam("ip-upload-multi") MultipartFile[] files, @RequestParam("moder-year-id") Long moderYearId, @RequestParam("color-id") Long colorId) throws IOException {
        UserDTO user = userService.getById(1L);
        DetailMotorDTO detailMotor = detailMotorService.getByModelYearAndColorMotor(moderYearId,colorId);
        System.out.println(detailMotor);
        PostDTO newPost = postService.savePost(post, user, detailMotor, ownershipSelect, files);
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }
}
