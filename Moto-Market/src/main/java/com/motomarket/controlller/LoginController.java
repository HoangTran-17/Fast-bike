package com.motomarket.controlller;

import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public ModelAndView toLoginView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView toSignUpView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/register");
        modelAndView.addObject("user",new UserDTO());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView toSignupView(@ModelAttribute UserDTO user){
        user.setRole(Role.USER);
        user.setUserStatus(StatusUser.ACTIVATE);
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        return modelAndView;
    }
}
