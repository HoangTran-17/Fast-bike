package com.motomarket.controlller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motomarket.service.dto.*;
import com.motomarket.service.motor.IBrandMotorService;
import com.motomarket.service.motor.ITypeMotorService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @Autowired
    IPostService postService;

    @Autowired
    IBrandMotorService brandMotorService;

    @Autowired
    ITypeMotorService typeMotorService;

    @GetMapping("/")
    public ModelAndView toHomePage(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        ModelAndView modelAndView = new ModelAndView("/index");
        if (!loginUsername.equals("0")) {
            UserDTO userLogin = userService.getByUserName(loginUsername);
            modelAndView.addObject("userLogin", userLogin);
            Long countPost = postService.getCountPost();
            modelAndView.addObject("count", countPost);
        }
        return modelAndView;
    }


    @PostMapping("/search-moto-home")
    public ModelAndView showListSearch(@RequestParam String br, String cc, String sr, Double pr, String pv, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("list-moto");
        Integer ccMin = null;
        Integer ccMax = null;
        Double prMin = 0.0;
        if (br.equals("-1")) {
            br = "";
        }
        if (cc.equals("-1")) {
            cc = null;
        } else {
            ccMin = Integer.valueOf(cc.substring(0, cc.indexOf("-")));
            ccMax = Integer.valueOf(cc.substring(cc.indexOf("-") + 1, cc.length()));

        }
        if (sr.equals("-1")) {
            sr = "";
        }
        if (pr == -1) {
            pr = null;
            prMin = null;
        }
        if (pv.equals("-1")) {
            pv = null;
        }
        String modelMotor = br + " " + sr;
        Page<PostDTO> postDTOPage = postService.findTopByFilters(PageRequest.of(pageable.getPageNumber(), 20), modelMotor, null, null, pv, null, ccMin, ccMax, prMin, pr, null, null);
        List<TypeMotorDTO> typeMotorList = typeMotorService.findAll();
        modelAndView.addObject("typeMotorList", typeMotorList);
        modelAndView.addObject("postList", postDTOPage);
        return modelAndView;
    }


    @GetMapping("/search-all")
    public ModelAndView searchAll(@RequestParam(value = "keyword", defaultValue = "null") String  keyword, Pageable pageable  ){
        ModelAndView modelAndView = new ModelAndView();
        if (keyword==null){
            modelAndView.setViewName("index");
        }else {
            Page<PostDTO> postDTOPage = postService.findTopByModelMotorIsLike(PageRequest.of(pageable.getPageNumber(), 20), keyword);
            modelAndView.setViewName("list-moto");
            List<TypeMotorDTO> typeMotorList = typeMotorService.findAll();
            modelAndView.addObject("typeMotorList", typeMotorList);
            modelAndView.addObject("postList", postDTOPage);
        }
        return modelAndView;
    }


}
