package com.motomarket.repository;

import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserByUserName(String userName);
    List<User> findAllByDeletedIsFalse() ;
}
