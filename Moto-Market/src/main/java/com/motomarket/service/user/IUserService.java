package com.motomarket.service.user;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.UserDTO;


public interface IUserService extends IGeneralService<UserDTO> {
    UserDTO findUserByEmail(String email);
}

