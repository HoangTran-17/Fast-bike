package com.motomarket.controlller;

import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/prolife/{id}")
    public ModelAndView showProlifeUser(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("profile-view");
        UserDTO userDTO = userService.getById(id);
        modelAndView.addObject("user", userDTO);
        return modelAndView;
    }
}
