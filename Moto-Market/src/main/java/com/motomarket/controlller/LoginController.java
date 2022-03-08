package com.motomarket.controlller;

import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private IUserService userService;


    @GetMapping("/signin")
    public ModelAndView toLoginView(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(loginUsername);
        if (loginUsername.equals("0")) {
            modelAndView.setViewName("/loginPage/login");
            modelAndView.addObject("user", new UserDTO());
        } else {
            UserDTO userLogin = userService.getByUserName(loginUsername);
            modelAndView.addObject("userLogin", userLogin);
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }


    @GetMapping("/signup")
    public ModelAndView toSignUpView() {
        ModelAndView modelAndView = new ModelAndView("/loginPage/register");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView handleSignUp(@Valid @ModelAttribute UserDTO user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView1 = new ModelAndView("/loginPage/register");
            modelAndView1.addObject("user", user);
            return modelAndView1;
        }
        user.setRole(Role.USER);
        user.setUserStatus(StatusUser.ACTIVATE);
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("/loginPage/login");
        return modelAndView;
    }

    @PostMapping("/signin")
    public ModelAndView handleSignIn(@ModelAttribute UserDTO user, @CookieValue(value = "loginUser", defaultValue = "0") String loginUsername, HttpServletResponse response, HttpServletRequest request) {
        UserDTO userDTO = userService.findUserByEmail(user.getEmail());
        ModelAndView modelAndView = new ModelAndView();
        if (userDTO != null) {
            if (userDTO.getPassword().equals(user.getPassword())) {
                loginUsername = userDTO.getUserName();
                // create cookie and set it in response
                Cookie cookie = new Cookie("loginUser", loginUsername);
                cookie.setMaxAge(24 * 60 * 60 * 30);
                response.addCookie(cookie);
                modelAndView.addObject("loginUser", userDTO);
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
