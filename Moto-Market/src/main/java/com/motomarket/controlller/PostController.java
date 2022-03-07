package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.motor.IDetailMotorService;
import com.motomarket.service.motor.IModelYearService;
import com.motomarket.service.post.IImageService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IDetailMotorService detailMotorService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private IModelYearService modelYearService;

    @GetMapping("/newpost")
    public ModelAndView toPostPage() {
        ModelAndView modelAndView = new ModelAndView("new-post");
        modelAndView.addObject("post", new PostDTO());
        return modelAndView;
    }

    @PostMapping("/newpost")
    public ModelAndView handlePost(@ModelAttribute PostDTO post, @RequestParam Long ownershipSelect, @RequestParam("ip-upload-multi") MultipartFile[] files, @RequestParam("moder-year-id") Long moderYearId, @RequestParam("color-id") Long colorId) throws IOException {
        UserDTO user = userService.getById(1L);
        DetailMotorDTO detailMotor = detailMotorService.getByModelYearAndColorMotor(moderYearId,colorId);
        System.out.println(detailMotor);
        PostDTO newPost = postService.savePost(post, user, detailMotor, ownershipSelect, files);
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @GetMapping("/detailpost/{postId}")
    public ModelAndView viewDetailPost(@PathVariable Long postId){
        PostDTO postDTO = postService.getById(postId);
        System.out.println(postDTO);
        List<ImageDTO> imageList = imageService.findAllByPostDTO(postDTO);
        DetailMotor detailMotor = detailMotorService.getDetailMotorById(postDTO.getDetailMotorDTO().getDetailMotorId());
        ModelAndView modelAndView = new ModelAndView("bike-detail");
        UserDTO userDTO = postDTO.getUserDTO();
        PrettyTime p = new PrettyTime(new Locale("vi"));
        String time = p.format(postDTO.getPostDate());
        String timeFormat = time.substring(0, 1).toUpperCase() + time.substring(1);
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        modelAndView.addObject("post", postDTO);
        modelAndView.addObject("images", imageList);
        modelAndView.addObject("detail", detailMotor);
        modelAndView.addObject("timePost", timeFormat);
        modelAndView.addObject("price", currencyFormatter.format(postDTO.getPrice()));
        return modelAndView;
    }


}
