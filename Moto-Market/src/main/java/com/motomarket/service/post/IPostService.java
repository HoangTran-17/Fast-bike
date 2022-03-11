package com.motomarket.service.post;

import com.motomarket.repository.model.Post;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService extends IGeneralService<PostDTO> {

    Long getCountPost();

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
    Page<PostDTO> findTopByFilters(int pageSize,String modelMotor,Integer modelYearMin, Integer modelYearMax,
                                   String province,String typeMotor,Integer capacityMin, Integer capacityMax,
                                   Double priceMin, Double priceMax,String kilometerCount, String colorMotor);

    //    List bài viết đang chờ (WAITING) của 1 user
    Page<PostDTO> findWaitingListByUserId(int pageSize, Long userId);

    //    List bài viết đang hiển thị (PUBLIC) của 1 user
    Page<PostDTO> findPublicListByUserId(int pageSize, Long userId);

    //    List bài viết đang bị ẩn (HIDE) của 1 user
    Page<PostDTO> findHideListByUserId(int pageSize, Long userId);

    //    List bài viết về xe đã bán (SOLD) của 1 user
    Page<PostDTO> findSoldListByUserId(int pageSize, Long userId);

    //    List tất cả bài viết của 1 user - trừ StatusPost.DELETE
    Page<PostDTO> findAllByUserId(int pageSize, Long userId);


    //    Mr Hữu
    PostResponse findPostDeletedIsFalseOrderByDate(Integer pageNo, Integer pageSize);
    PostResponse findPostWaitingOrderByDate(Integer pageNo, Integer pageSize);
    PostResponse findPostByTitleLikeOrDetailMotorLikeOrUserNameLikeOrStatusPostLikeOrKilometerCountLikeOrPriceLikeOrProvinceLikeOrDistrictLikeOrPostDateLikeOrOwnershipLike(String key,Integer pageNo, Integer pageSize);
}

