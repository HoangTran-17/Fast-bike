package com.motomarket.service.post;

import com.motomarket.repository.model.DetailMotor;
import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService extends IGeneralService<PostDTO> {

    Post savePost(Post post, User user, DetailMotor detailMotor, Long ownershipSelect, MultipartFile[] files);

    //    Lấy 12 bài mới nhất => trang chủ
    List<PostDTO> findTop12ByOrderByPostIdDesc();

   // Post savePost(Post post);
}

