package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

//        String modelMotor = "Honda Future 125";
//        List<PostDTO> postList1 = postService.findTopByModelMotorIsLike(modelMotor);
//        System.out.println(postList1);

//        Page<PostDTO> posts = postService.findTopByFilters("", null, null, 110, 150);
//        System.out.println(posts);

//        Page<PostDTO> posts = postService.findTopByProvince("Hà Nội");
//        System.out.println(posts);

//        TypeMotor typeMotor = new TypeMotor();
//        typeMotor.setTypeMotorName("Xe tay ga");
//        Page<Post> posts = postRepository.findTopByTypeMotor(Pageable.ofSize(20), "Xe tay ga", StatusPost.PUBLIC);
//        System.out.println(posts);

//        List<Post> postList = postRepository.findTopByStatusPost(StatusPost.PUBLIC, Pageable.ofSize(18));
//        System.out.println(postList);

    }





}
