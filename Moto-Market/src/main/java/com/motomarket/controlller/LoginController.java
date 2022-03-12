package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
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

    @Autowired
    private IPostRepository postRepository;


    @ModelAttribute("userLogin")
    public UserDTO getUserLoginFromCookie(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        UserDTO userLogin = null;
        if (!loginUsername.equals("0")) {
            userLogin = userService.getByUserName(loginUsername);
        }
        return userLogin;
    }


    @GetMapping("/signin")
    public ModelAndView toLoginView(@ModelAttribute("userLogin") UserDTO userLogin) {
        ModelAndView modelAndView = new ModelAndView();
        if (userLogin==null){
            modelAndView.setViewName("/loginPage/login");
            modelAndView.addObject("userLogin",new UserDTO());
        } else {
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

    @GetMapping("/test")
    public ModelAndView testController() {
        ModelAndView modelAndView = new ModelAndView("list-moto");
        return modelAndView;
    }

    @GetMapping("/test2")
    public ModelAndView testController2(@ModelAttribute("userLogin") UserDTO userLogin) {
        ModelAndView modelAndView = new ModelAndView("edit-post");

        return modelAndView;
    }

    @GetMapping("/test3")
    public ModelAndView testController3() {
        ModelAndView modelAndView = new ModelAndView("moto-manager");
        return modelAndView;
    }

    @GetMapping("/test4")
    public ModelAndView testController4() {
        ModelAndView modelAndView = new ModelAndView("user-view");
        return modelAndView;
    }


}
