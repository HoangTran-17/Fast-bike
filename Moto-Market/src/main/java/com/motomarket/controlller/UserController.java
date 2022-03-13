package com.motomarket.controlller;

import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.dto.UserView;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;


    @GetMapping("/view/{id}")
    public ModelAndView showUserView(@PathVariable Long id, Pageable pageable) {
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
}
