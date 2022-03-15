package com.motomarket.service.post;

import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.filter.CapacityFilter;
import com.motomarket.service.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService extends IGeneralService<PostDTO> {

    Long getCountPost();


    Long savePost(PostDTO postDTO, UserDTO user, DetailMotorDTO detailMotor, MultipartFile[] files);

    void update(PostDTO postDTO, MultipartFile[] files);

    //    List bài viết mới nhất
    List<PostDTO> findListOfLatestPosts(int listSize);

    //  List bài viết mới nhất, tìm kiếm theo seriesMotor -"Honda Future 125"
    List<PostDTO> findTopBySeriesMotor(int listSize, String seriesMotor);


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    Page<PostDTO> findTopByModelMotorIsLike(Pageable pageable, String modelMotor);

    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    Page<PostDTO> findTopByProvince(int pageSize,String province);

    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    Page<PostDTO> findTopByTypeMotor(int pageSize,String typeMotor);

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    Page<PostDTO> findTopByCapacity(int pageSize,int capacityMin, int capacityMax);

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    Page<PostDTO> findTopByFilters(Pageable pageable, String modelMotor, Integer modelYearMin, Integer modelYearMax,
                                   String province, String typeMotor, Integer capacityMin, Integer capacityMax,
                                   Double priceMin, Double priceMax, String kilometerCount, String colorMotor);

    //        Page<PostDTO> postDTOS = postService.findTopByFilters1(cc,prfr,prto,myfr,myto,km,color,pr);
    //
    //public ModelAndView showBikeList(String br, String tp, String cc, Double prfr, Double prto,
    //                                 int myfr, int myto, String km, String color, String pr, Pageable pageable) {


    Page<PostDTO> findTopByFilters1(Pageable pageable,String modelMotor, String brandMotor, String typeMotor, String capacity,
                                    Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax,
                                    String kilometerCount, String color, String province);

    //    List bài viết đang chờ (WAITING) của 1 user
    Page<PostDTO> findWaitingListByUserId(int pageSize, Long userId);


    //    List bài viết đang hiển thị (PUBLIC) của 1 user
    Page<PostDTO> findPublicListByUserId(Pageable pageable, Long userId);

    //     Số lượng bài viết đang hiển thị (Public) của 1 user
    int getCountPublicPostByUser(User user);

    //    List bài viết đang bị ẩn (HIDE) của 1 user
    Page<PostDTO> findHideListByUserId(int pageSize, Long userId);


    //    List bài viết về xe đã bán (SOLD) của 1 user
    Page<PostDTO> findSoldListByUserId(Pageable pageable, Long userId);

    //     Số lượng bài viết về xe đã bán (SOLD) của 1 user
    int getCountSoldPostByUser(User user);

    //    List tất cả bài viết của 1 user - trừ StatusPost.DELETE
    Page<PostDTO> findAllByUserId(int pageSize, Long userId);


    //    Mr Hữu
    PostResponse findPostDeletedIsFalseOrderByDate(Integer pageNo, Integer pageSize);

    PostResponse findPostWaitingOrderByDate(Integer pageNo, Integer pageSize);

    PostResponse findPostHideOrderByDate(Integer pageNo, Integer pageSize);

    PostResponse findPostByKeySearch(String key, Integer pageNo, Integer pageSize);

    PostResponse findWaitingPostByKeySearch(String key, Integer pageNo, Integer pageSize);

    PostResponse findHidePostByKeySearch(String key, Integer pageNo, Integer pageSize);

    void hide(Long id);
    void publicPost(Long id);
    void blockPost(Long id);

    void setSoldMoto(Long id);
    PostResponse findAllByUserIdAndStatusPost(Long userId, StatusPost statusPost,Integer pageNo, Integer pageSize );

    String[] setQueryView(String modelMotor, String brandMotor, String typeMotor, String capacity);

    List<CapacityFilter> getCapacityList(String modelMotor, String brandMotor, String typeMotor, String capacity);
}

