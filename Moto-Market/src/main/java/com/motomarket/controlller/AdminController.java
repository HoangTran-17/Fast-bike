package com.motomarket.controlller;

import com.motomarket.repository.model.Role;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public ModelAndView toAdminLoginPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin) {
        ModelAndView modelAndView = new ModelAndView();
        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            return modelAndView;
        } else {
            UserDTO adminLogin = userService.getByUserName(loginAdmin);
            System.out.println(adminLogin.getEmail());
            if (adminLogin.getRole().equals(Role.USER)) {
                modelAndView.setViewName("admin/login");
                modelAndView.addObject("messages", "Access denied!");
                return modelAndView;
            } else {
                modelAndView.setViewName("admin/users");
                System.out.println(adminLogin.getRole());
                modelAndView.addObject("adminLogin", adminLogin);
                modelAndView.addObject("users", getAllByRoleUser(adminLogin));
            }

        }
        return modelAndView;

    }

    @PostMapping("/process")
    public ModelAndView processLoginAdminPage(@ModelAttribute UserDTO admin, HttpServletResponse response, HttpServletRequest request) {
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
            // create cookie and set it in response
            Cookie cookie = new Cookie("loginAdmin", adminDTO.getUserName());
            cookie.setMaxAge(24 * 60 * 60 * 30);
            response.addCookie(cookie);
            modelAndView.addObject("users", getAllByRoleUser(adminDTO));
            System.out.println(adminDTO.getRole());
            modelAndView.addObject("adminLogin", adminDTO);
            return modelAndView;
        }
    }

    public List<UserDTO> getAllByRoleUser(UserDTO userDTO) {
        List<UserDTO> userDTOS = userService.findAllByDeletedIsFalse();
        if (userDTO.getRole().equals(Role.ADMIN)) {
            userDTOS.removeIf(u -> u.getRole().equals(Role.ADMIN) || u.getRole().equals(Role.DBA));
        } else {
            userDTOS.removeIf(u -> u.getRole().equals(Role.DBA));
        }

        return userDTOS;
    }

    @GetMapping("/logout")
    public ModelAndView doLogout(HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("admin/login");
        Cookie cookie = new Cookie("loginAdmin", "0");
        response.addCookie(cookie);
        return modelAndView;
    }
}
