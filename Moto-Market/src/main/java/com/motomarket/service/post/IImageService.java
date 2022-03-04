package com.motomarket.service.post;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;

import java.util.List;


public interface IImageService extends IGeneralService<ImageDTO> {
    List<ImageDTO> findAllByPostDTO(PostDTO postDTO);
}

