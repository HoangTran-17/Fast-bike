package com.codegym.service.dto;

import com.codegym.repository.model.Post;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ImageDTO {


    private Long imageId;

   // image
}
