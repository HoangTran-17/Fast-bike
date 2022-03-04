package com.motomarket.controlller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    @GetMapping("motor-management")
    public ModelAndView motorManagement() {
        ModelAndView modelAndView = new ModelAndView("/admin/motor-management");
        return modelAndView;
    }
}
