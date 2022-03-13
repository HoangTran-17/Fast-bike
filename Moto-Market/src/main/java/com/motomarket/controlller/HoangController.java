package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.dto.UserView;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import com.motomarket.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class HoangController {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;


    @GetMapping("/get")
    public void get() {

        List<PostDTO> postList1 = postService.findListOfLatestPosts(5);
        System.out.println(postList1);

        Long id = 110L;
        Post post = postRepository.getById(id);
        System.out.println(post);


        String modelMotor = "HONDA SH 150i";
        List<PostDTO> postServiceTopBySeriesMotor = postService.findTopBySeriesMotor(10, modelMotor);
        System.out.println(postServiceTopBySeriesMotor);

        List<Post> postList = postRepository.findTopBySeriesMotor(Pageable.ofSize(7), modelMotor, StatusPost.PUBLIC);
        System.out.println(postList);
//        List<PostDTO> postServiceListOfLatestPosts = postService.findListOfLatestPosts(15);
//        System.out.println(postServiceListOfLatestPosts);

//        Page<PostDTO> postServiceTopByFilters = postService.findTopByFilters(11,null,2008,2020, null, null, 110, 150,10000000.0,60000000.0,null,"trắng");
//        System.out.println(postServiceTopByFilters);
//
        Page<PostDTO> postServiceTopByProvince = postService.findTopByProvince(12, "Hà Nội");
        System.out.println(postServiceTopByProvince);

        Page<PostDTO> postServiceTopByModelMotorIsLike = postService.findTopByModelMotorIsLike(20, "Honda");
        System.out.println(postServiceTopByModelMotorIsLike);
//
        Page<PostDTO> postServiceTopByCapacity = postService.findTopByCapacity(4, 120, 150);
        System.out.println(postServiceTopByCapacity);
    }

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("set")
    public void set() {
        User user = userRepository.getById(1L);
        System.out.println(user);
        user.setCreated(new Date());
        userRepository.save(user);
    }

    @GetMapping("tests")
    public void test() {
        Post postDTO = postRepository.getById(120L);
        User userDTO = userRepository.getById(4L);
        postDTO.setUser(userDTO);
        postRepository.save(postDTO);
    }
}
