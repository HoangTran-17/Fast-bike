package com.motomarket.repository;

import com.motomarket.repository.model.TypeMotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeMotorRepository extends JpaRepository<TypeMotor, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'





}

