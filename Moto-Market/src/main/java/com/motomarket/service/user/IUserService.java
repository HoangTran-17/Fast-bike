package com.motomarket.service.user;

import com.motomarket.repository.model.User;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.UserDTO;
<<<<<<< HEAD
import com.motomarket.service.response.UserResponse;
import org.springframework.data.domain.Pageable;
=======
import com.motomarket.service.dto.UserView;
>>>>>>> hoang-dev

import java.util.List;


public interface IUserService extends IGeneralService<UserDTO> {
    UserDTO getByUserName(String userName);

    UserDTO findUserByEmail(String email);
    User save(User user);
    User getUserById(Long id);
    List<UserDTO> findAllByDeletedIsFalse() ;
    UserResponse findAllUserByDeletedIsFalseByDBA(Integer pageNo, Integer pageSize);
    UserResponse findAllUserByDeletedIsFalseByAdmin(Integer pageNo, Integer pageSize);
    UserResponse findUserByKeySearchByDBA(String key, Integer pageNo, Integer pageSize);
    UserResponse findUserByKeySearchByAdmin(String key, Integer pageNo, Integer pageSize);

    UserView getUserViewById(Long id);
}

