package com.motomarket.controlller;

import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.service.dto.*;
import com.motomarket.service.filter.BrandFilter;
import com.motomarket.service.filter.TypeMotorFilter;
import com.motomarket.service.motor.IBrandMotorService;
import com.motomarket.service.motor.IDetailMotorService;
import com.motomarket.service.motor.IModelYearService;
import com.motomarket.service.motor.ITypeMotorService;
import com.motomarket.service.post.IImageService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

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

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    IBrandMotorService brandMotorService;

    @Autowired
    ITypeMotorService typeMotorService;


    @ModelAttribute("userLogin")
    public UserDTO getUserLoginFromCookie(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        UserDTO userLogin = null;
        if (!loginUsername.equals("0")) {
            userLogin = userService.getByUserName(loginUsername);
        }
        return userLogin;
    }


    @GetMapping("/newpost")
    public String toPostPage(@ModelAttribute("userLogin") UserDTO userLogin, Model model) {
        if (userLogin == null) {
            return "redirect:/signin";
        } else {
            model.addAttribute("post", new PostDTO());
            model.addAttribute("userLogin", userLogin);
        }
        return "new-post";
    }

    @PostMapping("/newpost")
    public String handlePost(@ModelAttribute PostDTO post, @RequestParam("ip-upload-multi") MultipartFile[] files, @RequestParam("moder-year-id") Long moderYearId, @RequestParam("color-id") Long colorId, @ModelAttribute("userLogin") UserDTO userLogin) throws IOException {
        if (userLogin == null) {
            return "redirect:/signin";
        }
        DetailMotorDTO detailMotor = detailMotorService.getByModelYearAndColorMotor(moderYearId, colorId);
        Long newPostId = postService.savePost(post, userLogin, detailMotor, files);
        return "redirect:/post/detailpost/" + newPostId;
    }


    @GetMapping("/detailpost/{postId}")
    public Object viewDetailPost(@PathVariable Long postId, @ModelAttribute("userLogin") UserDTO userLogin) {
        PostDTO postDTO = postService.getById(postId);
        if (postDTO.getStatusPost() == StatusPost.BLOCKED || postDTO.getStatusPost() == StatusPost.SOLD || postDTO.getStatusPost() == StatusPost.DELETE || postDTO.getStatusPost() == StatusPost.HIDE) {
            return "redirect:/errors/404";
        } else {
            if (postDTO.getStatusPost() == StatusPost.WAITING) {
                if (userLogin != null) {
                    if (userLogin.getUserId() != postDTO.getUserDTO().getUserId()) {
                        return "redirect:/errors/404";
                    }
                } else {
                    return "redirect:/errors/404";
                }
            }
        }
        List<ImageDTO> imageList = imageService.findAllByPostDTO(postDTO);
        DetailMotorDTO detailMotorDTO = postDTO.getDetailMotorDTO();
        ModelAndView modelAndView = new ModelAndView("moto-detail");
        String seriesName = detailMotorDTO.getBrandMotor().getBrandName() + " " + detailMotorDTO.getSeriesMotor().getSeriesName();
        List<PostDTO> relatedPostDTO = postService.findTopBySeriesMotor(8, seriesName);
        List<PostDTO> newPostList = postService.findListOfLatestPosts(15);
        modelAndView.addObject("userLogin", userLogin);
        modelAndView.addObject("post", postDTO);
        modelAndView.addObject("images", imageList);
        modelAndView.addObject("detail", detailMotorDTO);
        modelAndView.addObject("postsRelated", relatedPostDTO);
        modelAndView.addObject("newPosts", newPostList);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, @ModelAttribute("userLogin") UserDTO userLogin) {
        PostDTO postDTO = postService.getById(id);
        if (userLogin == null) {
            return "redirect:/signin";
        } else {
            if (postDTO.getUserDTO().getUserId() == userLogin.getUserId()){
                postService.remove(id);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/sold-moto/{id}")
    public String setSoldMoto(@PathVariable Long id, @ModelAttribute("userLogin") UserDTO userLogin) {
        PostDTO postDTO = postService.getById(id);
        if (userLogin == null) {
            return "redirect:/signin";
        } else {
            if (postDTO.getUserDTO().getUserId() == userLogin.getUserId()){
                postService.setSoldMoto(id);
            }
        }
        return "redirect:/";
    }


    @GetMapping("/edit/{id}")
    public Object showEditPost(@PathVariable Long id, @ModelAttribute("userLogin") UserDTO userLogin) {
        PostDTO postDTO = postService.getById(id);
        if (userLogin == null) {
            return "redirect:/signin";
        } else {
            if (userLogin.getUserId() != postDTO.getUserDTO().getUserId()) {
                return "redirect:/errors/404";
            }
        }
        ModelAndView modelAndView = new ModelAndView("edit-post");
        modelAndView.addObject("userLogin", userLogin);
        modelAndView.addObject("post", postDTO);
        return modelAndView;
    }

    @PostMapping("/edit")
    public String hanlderEditPost(@ModelAttribute PostDTO post, @RequestParam("ip-upload-multi") MultipartFile[] files) {
        postService.update(post, files);
        return "redirect:/post/detailpost/" + post.getPostId();
    }

//    public Page<PostDTO> findTopByFilters1(Pageable pageable, String brandMotor, String typeMotor, String capacity,
//                                           Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax,
//                                           String kilometerCount, String color, String province) {
    @GetMapping("/moto-list")
    public ModelAndView showBikeList(@RequestParam(value = "q",required = false) String modelMotor,
                                     @RequestParam(value = "br",required = false) String brandMotor,
                                     @RequestParam(value = "tp",required = false) String typeMotor,
                                     @RequestParam(value = "cc",required = false) String capacity,
                                     @RequestParam(value = "pr-fr",required = false) Double priceFrom,
                                     @RequestParam(value = "pr-to",required = false) Double priceTo,
                                     @RequestParam(value = "my-fr",required = false) Integer modelYearMin,
                                     @RequestParam(value = "my-to",required = false) Integer modelYearMax,
                                     @RequestParam(value = "km",required = false) String kilometerCount,
                                     @RequestParam(value = "color",required = false) String color,
                                     @RequestParam(value = "pr",required = false) String province,
                                     Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list-moto");
        List<BrandFilter> brandList = brandMotorService.getAllBrandFilter(brandMotor, typeMotor, capacity);
        modelAndView.addObject("brandList", brandList);

        List<TypeMotorFilter> typeMotorList = typeMotorService.getAllTypeMotorFilter(brandMotor, typeMotor, capacity);
        modelAndView.addObject("typeMotorList", typeMotorList);

//        modelMotor = "HONDA";
        Page<PostDTO> postDTOS = postService.findTopByFilters1(PageRequest.of(pageable.getPageNumber(), 20),
                modelMotor,brandMotor, typeMotor, capacity,priceFrom,priceTo,
                modelYearMin,modelYearMax,kilometerCount,color,province);
        modelAndView.addObject("postList", postDTOS);

        System.out.println(postDTOS);

        return modelAndView;
    }

}
