package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//////Trang test của Hoàng, anh em cứ để vậy nha
@Controller
public class HoangController {

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




    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IPostService postService;


    @GetMapping("/get")
    public void get() {

        String modelMotor = "Honda Future 125";
        List<PostDTO> postServiceTopBySeriesMotor = postService.findTopBySeriesMotor(10,modelMotor);
        System.out.println(postServiceTopBySeriesMotor);

        List<PostDTO> postServiceListOfLatestPosts = postService.findListOfLatestPosts(15);
        System.out.println(postServiceListOfLatestPosts);

        Page<PostDTO> postServiceTopByFilters = postService.findTopByFilters(11,null, null, null, 110, 150);
        System.out.println(postServiceTopByFilters);
//
        Page<PostDTO> postServiceTopByProvince = postService.findTopByProvince(12,"Hà Nội");
        System.out.println(postServiceTopByProvince);

        Page<PostDTO> postServiceTopByModelMotorIsLike = postService.findTopByModelMotorIsLike(20, "Honda");
        System.out.println(postServiceTopByModelMotorIsLike);
//
        Page<PostDTO> postServiceTopByCapacity= postService.findTopByCapacity(4, 120, 150);
        System.out.println(postServiceTopByCapacity);
    }





}
