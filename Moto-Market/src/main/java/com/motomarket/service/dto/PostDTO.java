package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
import com.motomarket.service.user.UserService;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class PostDTO {

    private Long postId;

    @NotBlank
    private StatusPost statusPost;

    @NotBlank(message = "Vui lòng nhập tiêu đề bài đăng!")
    private String title;

    @NotBlank
    private String modelMotor;

    @NotBlank(message = "Vui lòng chọn khoảng km mà xe đã chạy!")
    private String kilometerCount;

    @NotBlank(message = "Vui lòng nhập thêm thông tin về tình trạng xe, giấy tờ,chủ xe,...!")
    private String description;

    @DecimalMin("1.0")
    @Column(columnDefinition="Decimal(12,2) default '0.00'")
    private Double price;

    @NotBlank(message = "Vui lòng nhập tên người bán!")
    private String sellerName;

    @NotBlank(message = "Vui lòng nhập số điện thoại! (VD: 0987654321)")
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})", message = "Vui lòng nhập đúng số điện thoại! (VD: 0987654321)")
    private String sellerPhoneNumber;

    @NotBlank(message = "Vui lòng chọn tỉnh!")
    private String province;

    @NotBlank(message = "Vui lòng chọn huyện!")
    private  String district;

    private Date postDate;

    @NotBlank
    private Ownership ownership;

    @NotBlank
    private UserDTO userDTO;

    @NotBlank
    private DetailMotorDTO detailMotorDTO;

    private List<ImageDTO> imageDTOList;

    private String timePeriod;

    public static PostDTO parsePostDTO(Post post) {
        UserDTO userDTO = UserDTO.parseUserDTO(post.getUser());
        DetailMotorDTO detailMotorDTO = DetailMotorDTO.parseDetailMotorDTO(post.getDetailMotor());

        List<ImageDTO> imageDTOList = new ArrayList<>();
        post.getImageList().forEach(image -> {
            imageDTOList.add(ImageDTO.parseImageDTO(image));
        });
        String timePeriod = calculateTheElapsedTime(post.getPostDate());

        return new PostDTO(post.getPostId(), post.getStatusPost(), post.getTitle(), post.getModelMotor(),
                post.getKilometerCount(), post.getDescription(), post.getPrice(),
                post.getSellerName(), post.getSellerPhoneNumber(), post.getProvince(), post.getDistrict(), post.getPostDate(), post.getOwnership(),
                userDTO,detailMotorDTO,imageDTOList,timePeriod);
    }

    private static String calculateTheElapsedTime(Date created) {
        Long untilNow = new Date().getTime() - created.getTime();

        Long SECOND = 1000L;
        Long MINUTE = 60 * SECOND;
        Long HOUR = 60 * MINUTE;
        Long DAY = 24 * HOUR;
        Long WEEK = 7 * DAY;
        Long MONTH = 30 * DAY;
        Long YEAR = 365 * DAY;
        Long[] list1 = {YEAR, MONTH, WEEK, DAY, HOUR, MINUTE};
        String[] list2 = {"năm", "tháng", "tuần", "ngày", "giờ", "phút"};

        int count = 0;
        String timePeriod = "";
        for (int i = 0; count == 0; ++i) {
            count = Math.round(untilNow / list1[i]) ;
            if (count > 0) {
                timePeriod = count + " " + list2[i];
                break;
            }
        }
        return timePeriod;
    }

}


