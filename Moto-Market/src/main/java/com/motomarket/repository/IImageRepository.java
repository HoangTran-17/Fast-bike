package com.motomarket.repository;

import com.motomarket.repository.model.Image;
import com.motomarket.repository.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPosts(Post post);
}
