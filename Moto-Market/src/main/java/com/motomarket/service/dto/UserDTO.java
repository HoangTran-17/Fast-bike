package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserDTO {


    private Long userId;

    private String avatar;

    @Column(unique = true)
    @NotNull(message = "Vui lòng nhập tên của bạn!")
    @Size(min = 2,max = 30,message = "Độ dài của tên trong khoảng từ 2 đến 30 ký tự!")
    private String userName;

    @NotNull(message = "Vui lòng nhập email của bạn!")
    @Pattern(regexp = "^[^@\\s]+@([^@\\s]+\\.)+[^@\\s]+$", message = "Vui lòng nhập đúng email!")
    private String email;

    private Role role;

    private StatusUser userStatus;

    private Date created;


    @NotNull(message = "Vui lòng nhập mật khẩu!")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,12}$", message = "Mật khẩu từ 6 đến 12 ký tự và chỉ sử dụng chữ cái in thường, in HOA và chữ số! ")
//    @Size(min = 6, max = 12, message = "Mật khẩu từ 6 đến 12 ký tự!")
    private String password;



    @NotNull(message = "Vui lòng nhập số điện thoại của bạn!")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})",message = "Vui lòng nhập đúng số điện thoại, VD: 0987654321")
    private String phoneNumber;

    private Integer numberOfPost;

    public UserDTO(Long userId) {
        this.userId = userId;
    }


    public static UserDTO parseUserDTO(User user) {
        return new UserDTO(user.getUserId(), user.getAvatar(), user.getUserName(),
                user.getEmail(), user.getRole(), user.getUserStatus(), user.getCreated(),
                user.getPassword(),user.getPhoneNumber(),user.getPostList().size());
    }
}

