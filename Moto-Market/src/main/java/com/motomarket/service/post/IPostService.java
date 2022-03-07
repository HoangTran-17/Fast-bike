package com.motomarket.service.post;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService extends IGeneralService<PostDTO> {

    PostDTO savePost(PostDTO post, UserDTO user, DetailMotorDTO detailMotor, Long ownershipSelect, MultipartFile[] files);

    //    Trang chủ - List 18 post mới nhất có statusPost là Public.
    List<PostDTO> findTop18ByOrderByPostIdDescAndStatusPostIsPublic();


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    List<PostDTO> findTopByModelMotorIsLike(String modelMotor);

//    Post savePost(Post post);
}

