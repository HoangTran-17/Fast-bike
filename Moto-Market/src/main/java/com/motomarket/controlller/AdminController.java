package com.motomarket.controlller;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IModelYearRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
//////Trang test của Hoàng, anh em cứ để vậy nha
@Controller
public class AdminController {

//    @Autowired
//    private IModelYearRepository modelYearRepository;
//    @Autowired
//    private IDetailMotorRepository detailMotorRepository;
//    @Autowired
//    private IPostRepository postRepository;
//    @Autowired
//    private IUserRepository userRepository;
//
//    @GetMapping("/insert")
//    public void insert() {
//        ModelYear modelYear = modelYearRepository.getById(63L);
//        List<DetailMotor> detailMotors = detailMotorRepository.findAllByModelYear(modelYear);
//        User user = userRepository.getById(2L);
//        detailMotors.forEach(detailMotor -> {
//            Post post = new Post();
//            post.setStatusPost(StatusPost.WAITING);
//            String modelMotor = detailMotor.getBrandMotor().getBrandName() + " " +
//                    detailMotor.getSeriesMotor().getSeriesName() + " " +
//                    modelYear.getModelYearName() + " " +
//                    detailMotor.getColorMotor().getColorName();
//                    ;
//            post.setTitle("Bán xe " + modelMotor);
//            post.setModelMotor(modelMotor);
//            post.setKilometerCount("Dưới 30,000km");
//            post.setDescription("Xe còn mới, đang sử dụng");
//            post.setPrice(4800000.0);
//            post.setSellerName(user.getUserName());
//            post.setSellerPhoneNumber(user.getPhoneNumber());
//            post.setProvince("Hà Nội");
//            post.setDistrict("Thanh Xuân");
//            post.setPostDate(new Date());
//            post.setOwnership(Ownership.OWNERSHIP);
//            post.setUser(user);
//            post.setDetailMotor(detailMotor);
//
//            postRepository.save(post);
//        });
//
//        System.out.println("Success");
//    }




//    @Autowired
//    private IPostService postService;

    @Autowired
    private IPostRepository postRepository;
    @GetMapping("/get")
    public void get() {
//                Page<Post> posts = postRepository.findAll(Pageable.ofSize(20));
//        System.out.println(posts);
        List<Post> postList = postRepository.findTop12ByOrderByPostIdDesc();
        System.out.println(postList);
    }


    @GetMapping("motor-management")
    public ModelAndView motorManagement() {

        ModelAndView modelAndView = new ModelAndView("/admin/motor-management");
        return modelAndView;
    }


}
