package com.motomarket.repository;

import com.motomarket.repository.model.ModelYear;
import com.motomarket.repository.model.SeriesMotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModelYearRepository extends JpaRepository<ModelYear, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'

    List<ModelYear> getAllBySeriesMotor(SeriesMotor seriesMotor);

    void deleteAllBySeriesMotor(SeriesMotor seriesMotor);
}

