package com.motomarket.service.user;

import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.UserDTO;

import java.util.List;


public interface IUserService extends IGeneralService<UserDTO> {
    UserDTO getByUserName(String userName);

    UserDTO findUserByEmail(String email);

    User getUserById(Long id);
    List<UserDTO> findAllByDeletedIsFalse() ;
}

