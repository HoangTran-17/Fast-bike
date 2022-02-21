package com.codegym.repository;

import com.codegym.repository.model.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetailMotorRepository extends JpaRepository<Posts, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'





}

