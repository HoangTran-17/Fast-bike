package com.motomarket.repository;

import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserByUserName(String userName);
    List<User> findAllByDeletedIsFalse();

    @Query("SELECT u FROM User u WHERE u.role <> 2 AND u.deleted=false ")
    Page<User> findAllUserByDeletedIsFalseByDBA(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role <> 2 AND u.role <> 1 AND u.deleted=false")
    Page<User> findAllUserByDeletedIsFalseByAdmin(Pageable pageable);

    @Query("SELECT u FROM User u WHERE "+ "("
            +" u.userName LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.phoneNumber LIKE %?1%"
            + " OR CONCAT(u.postList.size, '') LIKE %?1%"
            + " OR 'Active' LIKE %?1%"
            + " OR 'Block' LIKE %?1%"
            + " OR CONCAT(u.role, '') LIKE %?1%"
            +")"
            +" AND u.deleted = false "
            +" AND u.role <> 2 "
    )
    Page<User> findUserByKeySearchByDBA(String key, Pageable pageable);

    @Query("SELECT u FROM User u WHERE "+ "("
            +" u.userName LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.phoneNumber LIKE %?1%"
            + " OR CONCAT(u.postList.size, '') LIKE %?1%"
            + " OR 'Active' LIKE %?1%"
            + " OR 'Block' LIKE %?1%"
            + " OR CONCAT(u.role, '') LIKE %?1%"
            +")"
            +" AND u.deleted = false "
            +" AND u.role = 0 "
    )
    Page<User> findUserByKeySearchByAdmin(String key, Pageable pageable);
}
