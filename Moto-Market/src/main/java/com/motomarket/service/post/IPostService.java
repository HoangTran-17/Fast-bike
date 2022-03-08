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


    //    List bài viết mới nhất
    List<PostDTO> findListOfLatestPosts(int listSize);

    //  List bài viết mới nhất, tìm kiếm theo seriesMotor -"Honda Future 125"
    List<PostDTO> findTopBySeriesMotor(int listSize, String seriesMotor);

    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 "
    Page<PostDTO> findTopByModelMotorIsLike(int pageSize,String modelMotor);

    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    Page<PostDTO> findTopByProvince(int pageSize,String province);

    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    Page<PostDTO> findTopByTypeMotor(int pageSize,String typeMotor);

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    Page<PostDTO> findTopByCapacity(int pageSize,int capacityMin, int capacityMax);

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    Page<PostDTO> findTopByFilters(int pageSize,String modelMotor,int modelYearMin, int modelYearMax,
                                   String province,String typeMotor,int capacityMin, int capacityMax,
                                   Double priceMin, Double priceMax,String kilometerCount, String colorMotor);

//    Post savePost(Post post);
}

