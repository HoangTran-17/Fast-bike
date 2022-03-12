package com.motomarket.service.dto;

import com.motomarket.repository.model.User;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class UserView {

    private Long userId;
    private String avatar;
    private String userName;
    private Date created;
    private String timePeriod;
    private int countPublicPost;
    private int countSoldPost;

    public static UserView parseUserView(User user) {
        UserView userView = new UserView();
        userView.setUserId(user.getUserId());
        userView.setAvatar(user.getAvatar());
        userView.setUserName(user.getUserName());
        userView.setCreated(user.getCreated());

        return userView;
    }
}
