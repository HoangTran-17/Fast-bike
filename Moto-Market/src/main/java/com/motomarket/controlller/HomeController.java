package com.motomarket.controlller;

import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public ModelAndView toHomePage(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        ModelAndView modelAndView = new ModelAndView("/index");
        if (!loginUsername.equals("0")) {
            UserDTO userLogin = userService.getByUserName(loginUsername);
            modelAndView.addObject("userLogin", userLogin);
        }
        return modelAndView;
    }
}
