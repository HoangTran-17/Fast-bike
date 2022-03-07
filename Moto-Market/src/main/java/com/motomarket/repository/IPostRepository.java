package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
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


//    Trang chủ - List post mới nhất có yêu cầu về statusPost và số lượng post truyền vào từ Service.
    @Query("select p from Post p where p.statusPost = :statusPost order by p.postDate DESC")
    List<Post> findTopByStatusPost(StatusPost statusPost, Pageable pageable);

//  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    @Query("select p from Post p where p.statusPost = :statusPost and p.modelMotor like :modelMotor% order by p.postDate DESC")
    Page<Post> findTopByModelMotorIsLike(Pageable pageable,@Param("modelMotor") String modelMotor,@Param("statusPost") StatusPost statusPost);
}

