package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.motor.IDetailMotorService;
import com.motomarket.service.post.IImageService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDetailMotorService detailMotorService;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IImageService imageService;

    @GetMapping("/newpost")
    public ModelAndView toPostPage() {
        ModelAndView modelAndView = new ModelAndView("new-post");
        modelAndView.addObject("post", new PostDTO());
        return modelAndView;
    }

    @PostMapping("/newpost")
    public ModelAndView handlePost(@ModelAttribute PostDTO post, @RequestParam Long ownershipSelect, @RequestParam("ip-upload-multi") MultipartFile[] files, @RequestParam("moder-year-id") Long moderYearId, @RequestParam("color-id") Long colorId) throws IOException {
        UserDTO user = userService.getById(1L);
        DetailMotorDTO detailMotor = detailMotorService.getByModelYearAndColorMotor(moderYearId,colorId);
        System.out.println(detailMotor);
        PostDTO newPost = postService.savePost(post, user, detailMotor, ownershipSelect, files);
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping("/detailpost/{postId}")
    public ModelAndView viewDetailPost(@PathVariable Long postId){
        PostDTO postDTO = postService.getById(postId);
        System.out.println(postDTO);
        List<ImageDTO> imageList = imageService.findAllByPostDTO(postDTO);
        System.out.println(imageList);
        ModelAndView modelAndView = new ModelAndView("bike-detail");
        return modelAndView;
    }


}
