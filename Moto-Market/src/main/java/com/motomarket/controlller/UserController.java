package com.motomarket.controlller;

import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.dto.UserView;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;


    @ModelAttribute("userLogin")
    public UserDTO getUserLoginFromCookie(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        UserDTO userLogin = null;
        if (!loginUsername.equals("0")) {
            userLogin = userService.getByUserName(loginUsername);
        }
        return userLogin;
    }



    @GetMapping("/view/{id}")
    public ModelAndView showUserView(@PathVariable Long id, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        UserView userView = userService.getUserViewById(id);
        Page<PostDTO> publicList = postService.findPublicListByUserId(PageRequest.of(pageable.getPageNumber(), 12), id);
        Page<PostDTO> soldList = postService.findSoldListByUserId(pageable, id);
        modelAndView.setViewName("user-view");
        modelAndView.addObject("user", userView);
        modelAndView.addObject("publicList", publicList);
        modelAndView.addObject("soldList", soldList);
        return modelAndView;
    }


    @GetMapping("/my-account")
    public Object showMyAccountView(@ModelAttribute("userLogin") UserDTO userLogin) {
        if (userLogin==null){
            return "redirect:/signin";
        }
        ModelAndView modelAndView = new ModelAndView("info-account");
        modelAndView.addObject("userLogin", userLogin);
        return modelAndView;
    }

    @PostMapping("/update-profile")
    public Object updateProfile(@RequestParam("upLoadAvatar") MultipartFile[] files, @ModelAttribute("userLogin") UserDTO userLogin) {
        if (userLogin==null){
            return "redirect:/signin";
        }
        userService.updateAvatar(files, userLogin);
        ModelAndView modelAndView = new ModelAndView("info-account");
        modelAndView.addObject("userLogin", userLogin);
        modelAndView.addObject("message", "Cập nhật ảnh đại diện thành công!");
        return modelAndView;
    }

    @GetMapping("/change-password")
    public ModelAndView changePasswordShow(@ModelAttribute("userLogin") UserDTO userLogin){
        ModelAndView modelAndView = new ModelAndView("change-password");
        modelAndView.addObject("userLogin", userLogin);

        return modelAndView;
    }

    @GetMapping("/moto-manager")
    public Object showMotoManagerView(@ModelAttribute("userLogin") UserDTO userLogin) {
        if (userLogin==null){
            return "redirect:/signin";
        }
        ModelAndView modelAndView = new ModelAndView("moto-manager");
        modelAndView.addObject("userLogin", userLogin);
        return modelAndView;
    }


}
