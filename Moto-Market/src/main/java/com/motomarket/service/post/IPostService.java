package com.motomarket.service.post;

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
    Page<PostDTO> findTopByModelMotorIsLike(String modelMotor);

    Page<PostDTO> findTopByProvince(String province);

    Page<PostDTO> findTopByTypeMotor(String typemotor);

    Page<PostDTO> findTopByCapacity(int min, int max);

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    Page<PostDTO> findTopByFilters(String modelMotor, String province, String typeMotor, int min, int max);

//    Post savePost(Post post);
}

