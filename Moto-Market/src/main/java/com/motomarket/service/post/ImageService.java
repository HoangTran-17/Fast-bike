package com.motomarket.service.post;

import com.motomarket.repository.IImageRepository;
import com.motomarket.repository.model.Image;
import com.motomarket.repository.model.Post;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {
    @Autowired
    IImageRepository imageRepository;
    @Override
    public List<ImageDTO> findAll() {
        List<ImageDTO> imageDTOList = new ArrayList<>();
        imageRepository.findAll().forEach(image -> {
            imageDTOList.add(ImageDTO.parseImageDTO(image));
        });
        return imageDTOList;
    }

    @Override
    public ImageDTO getById(Long id) {
        Image image = imageRepository.getById(id);
        return ImageDTO.parseImageDTO(image);
    }

    @Override
    public ImageDTO save(ImageDTO imageDTO) {
        Image newImage = imageRepository.save(ImageDTO.parseImage(imageDTO));
        return ImageDTO.parseImageDTO(newImage);
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<ImageDTO> findAllByPostDTO(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(post.getPostId());
        List<ImageDTO> imageDTOList = new ArrayList<>();
        imageRepository.findAllByPost(post).forEach(image -> {
            imageDTOList.add(ImageDTO.parseImageDTO(image));
        });
        return imageDTOList;
    }
}
