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
    public ModelAndView showListSearch(@RequestParam String br, String cc, String sr, Double pr, String pv) {
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
        Page<PostDTO> postDTOPage = postService.findTopByFilters(5, modelMotor, null, null, pv, null, ccMin, ccMax, prMin, pr, null, null);
        System.out.println(postDTOPage.getContent());
        return modelAndView;
    }

    @GetMapping("/bike-list")
    public ModelAndView showBikeList(String br,String tp, String cc) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list-moto");
        br = "1_2_13";
        List<BrandFilter> brandList = brandMotorService.getAllBrandFilter(br, tp, cc);
        modelAndView.addObject("brandList", brandList);

        List<TypeMotorDTO> typeMotorList = typeMotorService.findAll();
        modelAndView.addObject("typeMotorList", typeMotorList);


        Page<PostDTO> postDTOS = postService.findTopByFilters(25, null, null, null, null,
                null, null, null, null, null, null, null);
        modelAndView.addObject("postList", postDTOS);

        System.out.println(postDTOS);

        return modelAndView;
    }
}
