package com.codegym.repository;

import com.codegym.repository.model.BrandMotor;
import com.codegym.repository.model.ColorMotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColorMotorRepository extends JpaRepository<ColorMotor, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'





}

