package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

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

    private String password;

    private String phoneNumber;

    private Long district;

    public UserDTO(Long userId) {
        this.userId = userId;
    }

    public static UserDTO parseUserDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(),
                user.getEmail(), user.getRole(), user.getUserStatus(),
                user.getPassword(), user.getPhoneNumber(),
                user.getDistrict().getDistrictId());
    }
}

