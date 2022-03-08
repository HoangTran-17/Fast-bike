package com.motomarket.repository;

import com.motomarket.repository.model.BrandMotor;
import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.dto.SeriesMotorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandMotorRepository extends JpaRepository<BrandMotor, Long> {

    //    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'

    BrandMotor getByBrandName(String brandName);
}

