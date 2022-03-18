package com.motomarket.repository;

import com.motomarket.repository.model.BrandMotor;
import com.motomarket.repository.model.SeriesMotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISeriesMotorRepository extends JpaRepository<SeriesMotor, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'

    List<SeriesMotor> getAllByBrandMotor(BrandMotor brandMotor);

    @Query("SELECT s FROM SeriesMotor s WHERE s.seriesName = :seriesName")
    SeriesMotor getBySeriesName(@Param("seriesName") String seriesName);

}

