package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.StatusUser;
import com.motomarket.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserByUserName(String userName);
    @Query("SELECT u FROM User u WHERE u.userStatus <> 2 ORDER BY u.created DESC ")
    List<User> findAllByDeletedIsFalse();

    @Query("SELECT u FROM User u WHERE u.role <> 2 AND u.userStatus <> 2  ORDER BY u.created DESC ")
    Page<User> findAllUserByDeletedIsFalseByDBA(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role <> 2 AND u.role <> 1 AND u.userStatus <> 2  ORDER BY u.created DESC ")
    Page<User> findAllUserByDeletedIsFalseByAdmin(Pageable pageable);

    @Query("SELECT u FROM User u WHERE "+ "("
            +" u.userName LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.phoneNumber LIKE %?1%"
            + " OR CONCAT(u.postList.size, '') LIKE %?1%"
            + " OR CONCAT(u.userStatus, '') LIKE %?1%"
            + " OR CONCAT(u.role, '') LIKE %?1%"
            +")"
            +" AND u.userStatus <> 2 "
            +" AND u.role <> 2   ORDER BY u.created DESC "
    )
    Page<User> findUserByKeySearchByDBA(String key, Pageable pageable);

    @Query("SELECT u FROM User u WHERE "+ "("
            +" u.userName LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.phoneNumber LIKE %?1%"
            + " OR CONCAT(u.postList.size, '') LIKE %?1%"
            + " OR CONCAT(u.userStatus, '') LIKE %?1%"
            + " OR CONCAT(u.role, '') LIKE %?1%"
            +")"
            +" AND u.userStatus <> 2 "
            +" AND u.role = 0  ORDER BY u.created DESC "
    )
    Page<User> findUserByKeySearchByAdmin(String key, Pageable pageable);

}
