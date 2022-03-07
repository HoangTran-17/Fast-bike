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

    @GetMapping("/admin/login")
    public ModelAndView toAdminLoginPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin) {
        ModelAndView modelAndView = new ModelAndView();

        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            modelAndView.addObject("admin",new UserDTO());
            return modelAndView;
        } else {

            UserDTO adminLogin = userService.getByUserName(loginAdmin);
            if (adminLogin.getRole().equals(Role.USER)) {
                modelAndView.setViewName("admin/login");
                modelAndView.addObject("messages", "Access denied!");
            } else {
                if (adminLogin.getRole().equals(Role.SUPER_ADMIN)) {
                    modelAndView.addObject("users", userService.findAllByDeletedIsFalse());
                } else {
                    modelAndView.addObject("users",userService.findAllByDeletedIsFalse().removeIf(userDTO -> userDTO.getRole().equals(Role.ADMIN)));
                }
                modelAndView.setViewName("admin/users");
                modelAndView.addObject("adminLogin", adminLogin);
            }
            return modelAndView;

        }

    }

    @PostMapping("admin/process")
    public ModelAndView doLoginAdminPage(@ModelAttribute UserDTO admin, @CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin, HttpServletResponse response, HttpServletRequest request) {
        UserDTO adminDTO = userService.findUserByEmail(admin.getEmail());
        ModelAndView modelAndView = new ModelAndView("admin/login");
        if (adminDTO == null) {
            modelAndView.addObject("messages", "Tài khoản không tồn tại!");
            return modelAndView;
        } else if (adminDTO.isDeleted()) {
            modelAndView.addObject("messages", "Tài khoản đã bị khóa!");
            return modelAndView;
        } else if (!admin.getPassword().equals(adminDTO.getPassword())) {
            modelAndView.addObject("messages", "Tài khoản hoặc mật khẩu không chính xác!");
            return modelAndView;
        } else if (adminDTO.getRole() == Role.USER) {
            modelAndView.addObject("messages", "Access denied!");
            return modelAndView;
        } else {
            modelAndView.setViewName("admin/users");
            loginAdmin = adminDTO.getUserName();
            // create cookie and set it in response
            Cookie cookie = new Cookie("loginAdmin", loginAdmin);
            cookie.setMaxAge(24 * 60 * 60 * 30);
            response.addCookie(cookie);
            modelAndView.addObject("adminLogin", adminDTO);
            if (adminDTO.getRole().equals(Role.SUPER_ADMIN)) {
                modelAndView.addObject("users", userService.findAllByDeletedIsFalse());
            } else {
                modelAndView.addObject("users",userService.findAllByDeletedIsFalse().removeIf(userDTO -> userDTO.getRole().equals(Role.ADMIN)));
            }

            return modelAndView;
        }


    }
}
