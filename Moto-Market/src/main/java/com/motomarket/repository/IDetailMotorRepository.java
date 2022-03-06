package com.motomarket.repository;

import com.motomarket.repository.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDetailMotorRepository extends JpaRepository<DetailMotor, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'


    DetailMotor getByModelYearAndColorMotor(ModelYear modelYear, ColorMotor colorMotor);

    List<DetailMotor> findAllByModelYear(ModelYear modelYear);

    void deleteAllBySeriesMotor(SeriesMotor seriesMotor);
}

