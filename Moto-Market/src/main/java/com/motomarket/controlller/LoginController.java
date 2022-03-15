package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

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
    public String toLoginView(@ModelAttribute("userLogin") UserDTO userLogin, Model model) {
        if (userLogin == null) {
            model.addAttribute("user", new UserDTO());
            return "/loginPage/login";
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/signup")
    public ModelAndView toSignUpView() {
        ModelAndView modelAndView = new ModelAndView("/loginPage/register");
        modelAndView.addObject("user", new UserDTO());
        return modelAndView;
    }

    @PostMapping("/signup")
    public String handleSignUp(@ModelAttribute UserDTO user) {
        Date date = new Date();
        user.setRole(Role.USER);
        user.setUserStatus(StatusUser.ACTIVATE);
        user.setCreated(date);
        userService.save(user);
        return "redirect:/signin";
    }

    @PostMapping("/signin")
    public Object handleSignIn(@ModelAttribute UserDTO user, @CookieValue(value = "loginUser", defaultValue = "0") String loginUsername, HttpServletResponse response, HttpServletRequest request) {
        UserDTO userDTO = userService.findUserByEmail(user.getEmail());
        ModelAndView modelAndView = new ModelAndView();
        if (userDTO != null) {
            if (userDTO.getPassword().equals(user.getPassword())) {
                loginUsername = userDTO.getUserName();
                // create cookie and set it in response
                Cookie cookie = new Cookie("loginUser", loginUsername);
                cookie.setMaxAge(24 * 60 * 60 * 30);
                response.addCookie(cookie);
                return "redirect:/";
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

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("loginUser", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
