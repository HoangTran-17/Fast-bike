package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'


    @Override
    Page<Post> findAll(Pageable pageable);

    //    Tìm bài viết theo brand motor (Tìm bằng ModelMotor bắt đầu với tên hãng xe).
//    @Query("select u from Post u where u.modelMotor like '1%'")
//    Page<Post> getAllByModelMotorStartingWith(String brandMotor);


//    Tìm bài viết theo p



}

