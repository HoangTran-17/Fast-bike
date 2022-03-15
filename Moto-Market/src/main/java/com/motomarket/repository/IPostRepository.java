package com.motomarket.repository;

import com.motomarket.repository.model.DetailMotor;
import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
import com.motomarket.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Override
    Page<Post> findAll(Pageable pageable);


    //    phân khối - tỉnh - hãng - loại xe
    @Query("select count(p) from Post p where p.statusPost = :statusPost")
    Long getCountPost(StatusPost statusPost);

    //    Trang chủ - List post mới nhất có yêu cầu về statusPost và số lượng post truyền vào từ Service.
    @Query("select p from Post p where p.statusPost = :statusPost order by p.postDate DESC")
    List<Post> findTopByStatusPost(StatusPost statusPost, Pageable pageable);


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125"
    @Query("select p from Post p where p.statusPost = :statusPost and p.modelMotor like :modelMotor% order by p.postDate DESC")
    List<Post> findTopBySeriesMotor(Pageable pageable, @Param("modelMotor") String modelMotor, @Param("statusPost") StatusPost statusPost);


    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    @Query("select p from Post p where p.statusPost = :statusPost and p.modelMotor like :modelMotor% order by p.postDate DESC")
    Page<Post> findTopByModelMotorIsLike(Pageable pageable, @Param("modelMotor") String modelMotor, @Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    @Query("select p from Post p where p.statusPost = :statusPost and p.province = :province order by p.postDate DESC")
    Page<Post> findTopByProvince(Pageable pageable, @Param("province") String province, @Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    @Query("select p from Post p where p.statusPost = :statusPost and p.detailMotor.typeMotor.typeMotorName = :typeMotor order by p.postDate DESC")
    Page<Post> findTopByTypeMotor(Pageable pageable, @Param("typeMotor") String typeMotor, @Param("statusPost") StatusPost statusPost);

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    @Query("select p from Post p where p.statusPost = :statusPost and (p.detailMotor.seriesMotor.capacity between :min and :max) order by p.postDate DESC")
    Page<Post> findTopByCapacity(Pageable pageable, @Param("min") int min, @Param("max") int max, @Param("statusPost") StatusPost statusPost);

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, modelYear, province, typeMotor, Capacity,price, kilometerCount và colorMotor .
    @Query("select p from Post p where p.statusPost = :statusPost " +
            "and (:modelMotor is null or p.modelMotor like :modelMotor%) " +
            "and ((:modelYearMin is null and :modelYearMax is null) " +
            "or (p.detailMotor.modelYear.modelYearName between :modelYearMin and :modelYearMax))" +
            "and (p.province = :province or :province is null) " +
            "and (:typeMotor is null or p.detailMotor.typeMotor.typeMotorName = :typeMotor)" +
            "and ((:capacityMin is null and :capacityMax is null) " +
            "or (p.detailMotor.seriesMotor.capacity between :capacityMin and :capacityMax))" +
            "and ((:priceMin is null and :priceMax is null) " +
            "or (p.price between :priceMin and :priceMax)) " +
            "and (:km is null or p.kilometerCount = :km)" +
            "and (:color is null or p.detailMotor.colorMotor.colorName = :color) " +
            "order by p.postDate DESC")
    Page<Post> findTopByFilters(
            Pageable pageable, @Param("modelMotor") String modelMotor,
            @Param("modelYearMin") Integer modelYearMin, @Param("modelYearMax") Integer modelYearMax,
            @Param("province") String province,
            @Param("typeMotor") String typeMotor,
            @Param("capacityMin") Integer capacityMin, @Param("capacityMax") Integer capacityMax,
            @Param("priceMin") Double priceMin, @Param("priceMax") Double priceMax,
            @Param("km") String kilometerCount,
            @Param("color") String colorMotor,
            @Param("statusPost") StatusPost statusPost);


    default Page<Post> findTopByFilters1(Pageable pageable,String modelMotor, List<Long> brandIdList, List<Long> typeIdList, StatusPost statusPost) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            Predicate predicateForModelMotor = criteriaBuilder.conjunction();
            if (modelMotor != null) {
                if (!brandIdList.isEmpty()) {
                    predicateForModelMotor = criteriaBuilder.or(root.get("detailMotor").get("brandMotor").get("brandId").in(brandIdList),
                                                                criteriaBuilder.like(root.get("modelMotor"), '%' + modelMotor + '%'));
                }else
                    predicateForModelMotor = criteriaBuilder.like(root.get("modelMotor"), '%' + modelMotor + '%');
            } else if (!brandIdList.isEmpty()) {
                predicateForModelMotor = root.get("detailMotor").get("brandMotor").get("brandId").in(brandIdList);
            }
            System.out.println(predicateForModelMotor);
            Predicate predicateForTypeIdList = criteriaBuilder.conjunction();
            if (!typeIdList.isEmpty()) {
                predicateForTypeIdList = root.get("detailMotor").get("typeMotor").get("typeMotorId").in(typeIdList);

            }
            Predicate predicateForStatusPost = root.get("statusPost").in(statusPost);

            return criteriaBuilder.and(predicateForModelMotor,predicateForTypeIdList,predicateForStatusPost);
        },pageable);
    }



















    default Page<Post> findBy(Pageable pageable,List<String> brandNames, Integer modelYearMin, Integer modelYearMax) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            Predicate predicateForBrandNames = criteriaBuilder.conjunction();
            if (!brandNames.isEmpty()) {
                predicateForBrandNames = root.get("detailMotor").get("brandMotor").get("brandName").in(brandNames);
            }

            Predicate predicateForModelYear = criteriaBuilder.conjunction();
            if (modelYearMin != null && modelYearMax != null) {
                predicateForModelYear = criteriaBuilder.between(root.get("detailMotor").get("modelYear").get("modelYearName"), modelYearMin, modelYearMax);
            } else {
                if (modelYearMin != null) {
                    predicateForModelYear = criteriaBuilder.greaterThan(root.get("detailMotor").get("modelYear").get("modelYearName"), modelYearMin);
                }
                if (modelYearMax != null) {
                    predicateForModelYear = criteriaBuilder.lessThan(root.get("detailMotor").get("modelYear").get("modelYearName"), modelYearMax);
                }
            }

            return criteriaBuilder.and(predicateForBrandNames,predicateForModelYear);
        },pageable);
    }


    @Query("select p from Post p where p.user = :user " +
            "and (:statusPost is null or p.statusPost = :statusPost) " +
            "order by p.postDate desc ")
    Page<Post> findTopByUser(Pageable pageable, @Param("user") User user,
                             @Param("statusPost") StatusPost statusPost);

    @Query("select count(p) from Post p where p.user = :user and p.statusPost = :statusPost")
    Integer countPostByStatusPostAndUser(@Param("user") User user,
                                         @Param("statusPost") StatusPost statusPost);


    @Query("select p from Post p where p.user = :user " +
            "and  p.statusPost <> :statusPost " +
            "order by p.postDate desc ")
    Page<Post> findTopByUserAndStatusPostNot(Pageable pageable, @Param("user") User user,
                                             @Param("statusPost") StatusPost statusPost);

    //    Mr Hữu
    @Query("SELECT p FROM Post p WHERE p.statusPost <> 3 AND p.statusPost <> 4 AND p.statusPost <> 5 ORDER BY p.postDate DESC ")
    Page<Post> findPostDeletedIsFalseOrderByDate(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.statusPost = 0  ORDER BY p.postDate ASC ")
    Page<Post> findPostWaitingOrderByDate(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.statusPost = 2  ORDER BY p.postDate DESC ")
    Page<Post> findPostHideOrderByDate(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE " + "("
            + " p.title LIKE %?1%"
            + " OR p.detailMotor.brandMotor.brandName LIKE %?1%"
            + " OR p.detailMotor.seriesMotor.seriesName LIKE %?1%"
            + " OR p.detailMotor.colorMotor.colorName LIKE %?1%"
            + " OR CONCAT(p.detailMotor.modelYear.modelYearName,'') LIKE %?1%"
            + " OR p.detailMotor.typeMotor.typeMotorName LIKE %?1%"
            + " OR p.user.userName LIKE %?1%"
            + " OR p.user.email LIKE %?1%"
            + " OR CONCAT(p.statusPost, '') LIKE %?1%"
            + " OR CONCAT(p.price, '') LIKE %?1%"
            + " OR CONCAT(p.postDate, '') LIKE %?1%"
            + " OR CONCAT(p.ownership, '') LIKE %?1%"
            + ")"
            + " AND p.statusPost <> 3"
            + " AND p.statusPost <> 4"
            + " AND p.statusPost <> 5"
    )
    Page<Post> findPostByKeySearch(String key, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE " + "(" +
            "p.title LIKE %?1%"
            + " OR p.detailMotor.brandMotor.brandName LIKE %?1%"
            + " OR p.detailMotor.seriesMotor.seriesName LIKE %?1%"
            + " OR p.detailMotor.colorMotor.colorName LIKE %?1%"
            + " OR CONCAT(p.detailMotor.modelYear.modelYearName,'') LIKE %?1%"
            + " OR p.detailMotor.typeMotor.typeMotorName LIKE %?1%"
            + " OR p.user.userName LIKE %?1%"
            + " OR p.user.email LIKE %?1%"
            + " OR CONCAT(p.price, '') LIKE %?1%"
            + " OR CONCAT(p.postDate, '') LIKE %?1%"
            + " OR CONCAT(p.ownership, '') LIKE %?1%"
            + ")"
            + " AND p.statusPost = 0"
    )
    Page<Post> findWaitingPostByKeySearch(String key, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE " + "(" +
            "p.title LIKE %?1%"
            + " OR p.detailMotor.brandMotor.brandName LIKE %?1%"
            + " OR p.detailMotor.seriesMotor.seriesName LIKE %?1%"
            + " OR p.detailMotor.colorMotor.colorName LIKE %?1%"
            + " OR CONCAT(p.detailMotor.modelYear.modelYearName,'') LIKE %?1%"
            + " OR p.detailMotor.typeMotor.typeMotorName LIKE %?1%"
            + " OR p.user.userName LIKE %?1%"
            + " OR p.user.email LIKE %?1%"
            + " OR CONCAT(p.price, '') LIKE %?1%"
            + " OR CONCAT(p.postDate, '') LIKE %?1%"
            + " OR CONCAT(p.ownership, '') LIKE %?1%"
            + ")"
            + " AND p.statusPost = 2"
    )
    Page<Post> findHidePostByKeySearch(String key, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId AND p.statusPost = :statusPost")
    Page<Post> findAllByUserIdAndStatusPost(@Param("userId") Long id, @Param("statusPost") StatusPost statusPost, Pageable pageable);
}




