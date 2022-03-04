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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private IUserService userService;


    @GetMapping("/signin")
    public ModelAndView toLoginView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        modelAndView.addObject("user",new UserDTO());
        return modelAndView;
    }


    @GetMapping("/signup")
    public ModelAndView toSignUpView(){
        ModelAndView modelAndView = new ModelAndView("/loginPage/register");
        modelAndView.addObject("user",new UserDTO());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView handleSignUp(@ModelAttribute UserDTO user){
        user.setRole(Role.USER);
        user.setUserStatus(StatusUser.ACTIVATE);
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        return modelAndView;
    }

    @PostMapping("/signin")
    public ModelAndView handleSignIn(@ModelAttribute UserDTO user){
       UserDTO userDTO = userService.findUserByEmail(user.getEmail());
       ModelAndView  modelAndView = new ModelAndView();
       if (userDTO!=null){
           if (userDTO.getPassword().equals(user.getPassword())){
               modelAndView.setViewName("index");
           } else {
               modelAndView.addObject("message", "Đăng nhập không thành công vui lòng thử lại!");
               modelAndView.addObject("user", new UserDTO());
               modelAndView.setViewName("/loginPage/login");
           }
       } else {
           modelAndView.addObject("message", "Đăng nhập không thành công vui lòng thử lại!");
           modelAndView.addObject("user", new UserDTO());
           modelAndView.setViewName("/loginPage/login");
       }
       return modelAndView;
    }

//    @GetMapping("/test")
//    public ModelAndView testController(){
//        final String uri = "https://vapi.vnappmob.com/api/province/district/44";
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(uri, String.class);
//        System.out.println(result);
//        ModelAndView modelAndView = new ModelAndView("index");
//        return modelAndView;
//    }



}
