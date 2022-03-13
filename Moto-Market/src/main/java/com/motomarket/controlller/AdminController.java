package com.motomarket.controlller;

import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
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

    @Autowired
    private IPostService postService;

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
                modelAndView.setViewName("admin/home");
                modelAndView.addObject("adminLogin", adminLogin);
            }

        }
        return modelAndView;

    }

    @PostMapping("/success")
    public ModelAndView processLoginAdminPage(@ModelAttribute UserDTO admin, HttpServletResponse response, HttpServletRequest request) {
        UserDTO adminDTO = userService.findUserByEmail(admin.getEmail());
        ModelAndView modelAndView = new ModelAndView("admin/login");
        if (adminDTO == null) {
            modelAndView.addObject("messages",
                    "Account does not exist!");
            return modelAndView;
        } else if (!adminDTO.getUserStatus().equals(StatusUser.ACTIVATE)) {
            modelAndView.addObject("messages", "Account has been locked!");
            return modelAndView;
        } else if (!admin.getPassword().equals(adminDTO.getPassword())) {
            modelAndView.addObject("messages", "Incorrect account or password!");
            return modelAndView;
        } else if (adminDTO.getRole() == Role.USER) {
            modelAndView.addObject("messages", "Access denied!");
            return modelAndView;
        } else {
            modelAndView.setViewName("admin/home");
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
    @GetMapping("/list-user")
    public ModelAndView toListUserPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin) {
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
                modelAndView.setViewName("admin/list-user");
                modelAndView.addObject("adminLogin", adminLogin);

            }

        }
        return modelAndView;

    }

    @GetMapping("/add-new-admin")
    public ModelAndView toAddNewAdminPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin) {
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
                modelAndView.setViewName("admin/add-new-admin");
                modelAndView.addObject("adminLogin", adminLogin);

            }

        }
        return modelAndView;

    }

    @GetMapping("/post")
    public ModelAndView toAllPostPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("admin/all-post");
        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            modelAndView.addObject("messages","Bạn hãy đăng nhập tài khoản admin");
            return modelAndView;
        } else {
            UserDTO adminLogin = userService.getByUserName(loginAdmin);
            modelAndView.addObject("adminLogin",adminLogin);
            return modelAndView;
        }

    }

    @GetMapping("/post/waiting")
    public ModelAndView toWaitingPostPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("admin/waiting-post");
        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            modelAndView.addObject("messages","Bạn hãy đăng nhập tài khoản admin");
            return modelAndView;
        } else {
            UserDTO adminLogin = userService.getByUserName(loginAdmin);
            modelAndView.addObject("adminLogin",adminLogin);
            return modelAndView;
        }

    }

    @GetMapping("/post/hide")
    public ModelAndView toHidePostPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("admin/hide-post");
        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            modelAndView.addObject("messages","Bạn hãy đăng nhập tài khoản admin");
            return modelAndView;
        } else {
            UserDTO adminLogin = userService.getByUserName(loginAdmin);
            modelAndView.addObject("adminLogin",adminLogin);
            return modelAndView;
        }

    }

    @GetMapping("/post-detail/{postId}")
    public ModelAndView toPostDetailPage(@CookieValue(value = "loginAdmin", defaultValue = "0") String loginAdmin,
                                         HttpServletResponse response, @PathVariable Long postId) {
        ModelAndView modelAndView = new ModelAndView("admin/post-detail");
        PostDTO postDTO = postService.getById(postId);
        UserDTO adminLogin = userService.getByUserName(loginAdmin);
        if (loginAdmin.equals("0")) {
            modelAndView.setViewName("admin/login");
            modelAndView.addObject("messages","Bạn hãy đăng nhập tài khoản admin");
            return modelAndView;
        } else if (!postDTO.getStatusPost().equals(StatusPost.DELETE)){
            modelAndView.addObject("adminLogin",adminLogin);
            modelAndView.addObject("post",postDTO);
            return modelAndView;
        } else {
            modelAndView.setViewName("admin/all-post");
            modelAndView.addObject("adminLogin",adminLogin);
            return modelAndView;
        }

    }

}
