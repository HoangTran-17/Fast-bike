package com.motomarket.service.post;

import com.motomarket.repository.model.Image;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import org.apache.commons.lang3.math.IEEE754rUtils;

import java.util.List;


public interface IImageService extends IGeneralService<ImageDTO> {
//    List<ImageDTO> findAllByPostDTO(PostDTO postDTO);

    Image saveImage(Image image);
}

