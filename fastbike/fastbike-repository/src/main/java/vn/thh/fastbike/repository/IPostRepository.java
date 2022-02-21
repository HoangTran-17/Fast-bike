package vn.thh.fastbike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.thh.fastbike.repository.model.Post;

import java.util.List;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {

//    @Query("SELECT u " +
//            "from User u " +
//            "JOIN u.workspaces w " +
//            "where w.id = :id")
//    List<User> getAllUserByWorkspaceId(@Param("id") Long id);'a%'

//    Tìm bài viết theo brand motor (Tìm bằng ModelMotor bắt đầu với tên hãng xe).
    @Query("select u from Posts u where u.modelMotor like '1%'")
    List<Post> getAllByModelMotorStartingWith(String brandMotor);


//    Tìm bài viết theo p



}

