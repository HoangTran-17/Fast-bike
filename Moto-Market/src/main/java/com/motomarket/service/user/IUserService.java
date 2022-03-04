package com.motomarket.service.user;

import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.UserDTO;


public interface IUserService extends IGeneralService<UserDTO> {
    UserDTO findUserByEmail(String email);

    User getUserById(Long id);
}

