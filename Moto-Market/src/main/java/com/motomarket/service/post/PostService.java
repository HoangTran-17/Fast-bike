package com.motomarket.service.post;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.DetailMotor;
import com.motomarket.repository.model.Post;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService implements IPostService{
    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IDetailMotorRepository detailMotorRepository;
    @Autowired
    private IUserRepository userRepository;

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
    public Post savePost(Post post) {
        return postRepository.save(post);
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
