package com.motomarket.service.dto;

import com.motomarket.repository.model.Image;
import com.motomarket.repository.model.Post;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ImageDTO {


    private Long imageId;

    private String imageName;

    private Long postId;

    public static ImageDTO parseImageDTO(Image image) {
        return new ImageDTO(image.getImageId(), image.getImageName(), image.getPosts().getPostId());
    }

    public static Image parseImage(ImageDTO imageDTO) {
        Post post = new Post();
        post.setPostId(imageDTO.getPostId());
        return new Image(imageDTO.getImageId(), imageDTO.getImageName(), post);
    }
}
