package com.motomarket.repository;

import com.motomarket.repository.model.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpecificationsRepository extends JpaRepository<Specifications,Long> {
}
