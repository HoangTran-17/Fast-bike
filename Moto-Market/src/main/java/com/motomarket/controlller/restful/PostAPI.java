package com.motomarket.controlller.restful;

import com.motomarket.repository.model.StatusPost;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostAPI {
    @Autowired
    private IPostService postService;

    @Autowired
    private IUserService userService;

    @ModelAttribute("userLogin")
    public UserDTO getUserLoginFromCookie(@CookieValue(value = "loginUser", defaultValue = "0") String loginUsername) {
        UserDTO userLogin = null;
        if (!loginUsername.equals("0")) {
            userLogin = userService.getByUserName(loginUsername);
        }
        return userLogin;
    }


    @GetMapping("/getListOfLatestPosts")
    public ResponseEntity<List<PostDTO>> GetListOfLatestPosts() {
        List<PostDTO> postDTOs = postService.findListOfLatestPosts(18);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{statusPost}")
    public Object getListPostByUserIdAndStatusPost(@PathVariable Long userId, @PathVariable StatusPost statusPost, @RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize, @ModelAttribute("userLogin") UserDTO userLogin) {
        if (userLogin == null) {
            return "redirect:/signin";
        } else{
            if (userId == userLogin.getUserId()){
                return  postService.findAllByUserIdAndStatusPost(userId, statusPost, pageNo, pageSize);
            }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
