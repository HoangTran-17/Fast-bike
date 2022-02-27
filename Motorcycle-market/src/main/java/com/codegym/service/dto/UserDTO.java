package com.codegym.service.dto;

import com.codegym.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserDTO {

    private Long userId;

    private String userName;

    private String email;

    private Role role;

    private StatusUser userStatus;

//    private String password;

    private String phoneNumber;

    public UserDTO(Long userId) {
        this.userId = userId;
    }

    public static UserDTO parseUserDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(), user.getEmail(), user.getRole(), user.getUserStatus(), user.getPhoneNumber());
    }
}

