package com.motomarket.service.post;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.PostDTO;

import java.util.List;

public interface IPostService extends IGeneralService<PostDTO> {

    List<PostDTO> findTop12ByOrderByPostIdDesc();
}
