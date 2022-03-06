package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

//    List 12 records latest - Trang chủ
    List<Post> findTop12ByOrderByPostIdDesc();

    List<Post> findTop18ByOrderByPostIdDescStatusPost(StatusPost statusPost);

    Page<Post> findAllByModelMotorBefore(Pageable pageable, String modelMotor);




}

