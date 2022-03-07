package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.TypeMotor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'


    @Override
    Page<Post> findAll(Pageable pageable);

//    phân khối - tỉnh - hãng - loại xe

//    Trang chủ - List post mới nhất có yêu cầu về statusPost và số lượng post truyền vào từ Service.
    @Query("select p from Post p where p.statusPost = :statusPost order by p.postDate DESC")
    List<Post> findTopByStatusPost(StatusPost statusPost, Pageable pageable);


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125"
    @Query("select p from Post p where p.statusPost = :statusPost and p.modelMotor like :modelMotor% order by p.postDate DESC")
    List<Post> findTopBySeriesMotor(Pageable pageable,@Param("modelMotor") String modelMotor,@Param("statusPost") StatusPost statusPost);


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    @Query("select p from Post p where p.statusPost = :statusPost and p.modelMotor like :modelMotor% order by p.postDate DESC")
    Page<Post> findTopByModelMotorIsLike(Pageable pageable,@Param("modelMotor") String modelMotor,@Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    @Query("select p from Post p where p.statusPost = :statusPost and p.province = :province order by p.postDate DESC")
    Page<Post> findTopByProvince(Pageable pageable,@Param("province") String province,@Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    @Query("select p from Post p where p.statusPost = :statusPost and p.detailMotor.typeMotor.typeMotorName = :typeMotor order by p.postDate DESC")
    Page<Post> findTopByTypeMotor(Pageable pageable, @Param("typeMotor") String typeMotor, @Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    @Query("select p from Post p where p.statusPost = :statusPost and (p.detailMotor.seriesMotor.capacity between :min and :max) order by p.postDate DESC")
    Page<Post> findTopByCapacity(Pageable pageable, @Param("min") int min, @Param("max") int max, @Param("statusPost") StatusPost statusPost);

//    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    @Query("select p from Post p where p.statusPost = :statusPost and (:modelMotor is null or p.modelMotor like :modelMotor%) " +
            "and (p.province = :province or :province is null) and (:typeMotor is null or p.detailMotor.typeMotor.typeMotorName = :typeMotor) " +
            "and ((:min is null and :max is null) or (p.detailMotor.seriesMotor.capacity between :min and :max)) order by p.postDate DESC")
    Page<Post> findTopByModelMotorIsLikeAAndProvinceAndTypeMotorAndCapacity(Pageable pageable,@Param("modelMotor") String modelMotor,
                                                                            @Param("province") String province,@Param("typeMotor") String typeMotor,
                                                                            @Param("min") Integer min, @Param("max") Integer max,
                                                                            @Param("statusPost") StatusPost statusPost);
}

