package com.codegym.repository;

import com.codegym.repository.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetailMotorRepository extends JpaRepository<DetailMotor, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'


    DetailMotor getByModelYearAndColorMotor(ModelYear modelYear, ColorMotor colorMotor);

    void deleteAllBySeriesMotor(SeriesMotor seriesMotor);
}

