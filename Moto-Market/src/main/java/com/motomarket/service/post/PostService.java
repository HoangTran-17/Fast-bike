package com.motomarket.service.post;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService implements IPostService{
    @Value("${server.rootPath}")
    private String rootPath;

    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IDetailMotorRepository detailMotorRepository;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    IImageService imageService;

    @Override
    public List<PostDTO> findAll() {
        List<PostDTO> postDTOList = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }

    @Override
    public PostDTO getById(Long id) {
        return PostDTO.parsePostDTO(postRepository.getById(id));
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        Post post = parsePost(postDTO);
        PostDTO newPostDTO = PostDTO.parsePostDTO( postRepository.save(post));
        return newPostDTO;
    }

    @Override
    public Post savePost(Post post, User user, DetailMotor detailMotor, Long ownershipSelect, MultipartFile[] files)  {
        Date date = new Date();
        post.setStatusPost(StatusPost.PUBLIC);
        post.setPostDate(date);
        if (ownershipSelect == 0) {
            post.setOwnership(Ownership.OWNERSHIP);
        } else {
            post.setOwnership(Ownership.NO_OWNERSHIP);
        }

        post.setUser(user);

        post.setDetailMotor(detailMotor);
        Post newPost = postRepository.save(post);
        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            Image image = new Image();
            image.setImageName(uuid);
            image.setPosts(newPost);
            try {
                file.transferTo(new File(rootPath + "/" + uuid+".png"));
            } catch (IOException e) {
                throw  new IllegalArgumentException(e);
            }
            imageService.saveImage(image);
        }
        return newPost;
    }

    private Post parsePost(PostDTO postDTO) {
        Long detailMotorId = postDTO.getDetailMotorDTO().getDetailMotorId();
        DetailMotor detailMotor = detailMotorRepository.getById(detailMotorId);
        User user = userRepository.getById(postDTO.getUserDTO().getUserId());

        return new Post(postDTO.getStatusPost(), postDTO.getTitle(),
                postDTO.getModelMotor(), postDTO.getKilometerCount(), postDTO.getDescription(),
                postDTO.getPrice(), postDTO.getSellerName(), postDTO.getSellerPhoneNumber(),
                postDTO.getProvince(), postDTO.getDistrict(),
                postDTO.getPostDate(), postDTO.getOwnership(),user,detailMotor);
    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public List<PostDTO> findTop12ByOrderByPostIdDesc() {
        List<PostDTO> postDTOList = new ArrayList<>();
        postRepository.findTop12ByOrderByPostIdDesc().forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }
}
