package com.codegym.repository;

import com.codegym.repository.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostsRepository extends JpaRepository<Posts, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'

//    Tìm bài viết theo brand motor (Tìm bằng ModelMotor bắt đầu với tên hãng xe).
    @Query("select u from Posts u where u.modelMotor like '1%'")
    List<Posts> getAllByModelMotorStartingWith(String brandMotor);


//    Tìm bài viết theo p


}

