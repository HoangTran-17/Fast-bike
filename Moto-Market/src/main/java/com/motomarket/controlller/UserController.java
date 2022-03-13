package com.motomarket.controlller;

import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.dto.UserView;
import com.motomarket.service.post.IImageService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;


    @GetMapping("/view/{id}")
    public ModelAndView showUserView(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        UserView userView = userService.getUserViewById(id);
        Page<PostDTO> publicList = postService.findPublicListByUserId(12, id);
        Page<PostDTO> soldList = postService.findSoldListByUserId(10, id);
        modelAndView.setViewName("user-view");
        modelAndView.addObject("user", userView);
        modelAndView.addObject("publicList", publicList);
        modelAndView.addObject("soldList", soldList);
        return modelAndView;
    }


    @GetMapping("/my-account")
    public ModelAndView showMyAccountView() {
        UserDTO userDTO = userService.getById(1L);
        ModelAndView modelAndView = new ModelAndView("info-account");
        modelAndView.addObject("userLogin", userDTO);
        return modelAndView;
    }

    @PostMapping("/update-profile")
    public ModelAndView updateProfile(@RequestParam("upLoadAvatar") MultipartFile[] files) {
        UserDTO userDTO = userService.getById(1L);
        userService.updateAvatar(files, userDTO);
        ModelAndView modelAndView = new ModelAndView("info-account");
        modelAndView.addObject("userLogin", userDTO);
        modelAndView.addObject("message", "Cập nhật ảnh đại diện thành công!");
        return modelAndView;
    }

    @GetMapping("/moto-manager")
    public ModelAndView showMotoManagerView() {
        ModelAndView modelAndView = new ModelAndView("moto-manager");
        return modelAndView;
    }


}
