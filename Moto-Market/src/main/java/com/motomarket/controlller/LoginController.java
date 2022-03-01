package com.motomarket.controlller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping("/")
    public ModelAndView toLoginView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView toSignUpView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/register");
        return modelAndView;
    }
}
