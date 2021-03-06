package com.motomarket.repository;


import com.motomarket.repository.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SampleRepository extends JpaRepository<Sample, Integer>, JpaSpecificationExecutor<Sample> {

    @Query("select s from Sample s where s.id in :ids")
    List<Sample> queryIn(@Param("ids") List<Integer> ids);

    List<Sample> findByIdIn(List<Integer> ids);

    default List<Sample> findIn(List<Integer> ids) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {
            if (ids.isEmpty()) {
                return null; // or criteriaBuilder.conjunction()
            } else {
                return root.get("id").in(ids);
            }
        });
    }
}
