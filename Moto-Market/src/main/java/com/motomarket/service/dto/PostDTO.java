package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class PostDTO {

    private Long postId;

    private StatusPost statusPost;

    @NotBlank(message = "Vui lòng nhập tiêu đề bài đăng!")
    private String title;

    @NotBlank
    private String modelMotor;

    @NotBlank
    private String kilometerCount;

    @NotBlank(message = "Vui lòng nhập thêm thông tin về tình trạng xe, giấy tờ,chủ xe,...!")
    private String description;

    @NotBlank
    private Double price;

    @NotBlank(message = "Vui lòng nhập tên người bán!")
    private String sellerName;

    @NotBlank(message = "Vui lòng nhập số điện thoại! (VD: 0987654321)")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})", message = "Vui lòng nhập đúng số điện thoại! (VD: 0987654321)")
    private String sellerPhoneNumber;

    @NotBlank
    private Date postDate;

    private Ownership ownership;

    private UserDTO userDTO;

    private DetailMotorDTO detailMotorDTO;

    private DistrictDTO districtDTO;

    public static PostDTO parsePostDTO(Post post) {
        UserDTO userDTO = UserDTO.parseUserDTO(post.getUser());
        DetailMotorDTO detailMotorDTO = DetailMotorDTO.parseDetailMotorDTO(post.getDetailMotor());
        DistrictDTO districtDTO = DistrictDTO.parseDistrictDTO(post.getDistrict());

        return new PostDTO(post.getPostId(), post.getStatusPost(), post.getTitle(), post.getModelMotor(),
                post.getKilometerCount(), post.getDescription(), post.getPrice(),
                post.getSellerName(), post.getSellerPhoneNumber(), post.getPostDate(), post.getOwnership(),
                userDTO,detailMotorDTO, districtDTO);
    }

}


