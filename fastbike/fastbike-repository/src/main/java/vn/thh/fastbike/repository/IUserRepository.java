package vn.thh.fastbike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.thh.fastbike.repository.model.User;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);

    @Query("SELECT u " +
            "from User u " +
            "JOIN u.workspaces w " +
            "where w.id = :id")
    List<User> getAllUserByWorkspaceId(@Param("id") Long id);
}
