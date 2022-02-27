package com.codegym.service.post;

import com.codegym.repository.IDetailMotorRepository;
import com.codegym.repository.IDistrictRepository;
import com.codegym.repository.IPostRepository;
import com.codegym.repository.IUserRepository;
import com.codegym.repository.model.DetailMotor;
import com.codegym.repository.model.District;
import com.codegym.repository.model.Post;
import com.codegym.repository.model.User;
import com.codegym.service.dto.PostDTO;
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
    private IDistrictRepository districtRepository;
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

    private Post parsePost(PostDTO postDTO) {
        Long detailMotorId = postDTO.getDetailMotorDTO().getDetailMotorId();
        DetailMotor detailMotor = detailMotorRepository.getById(detailMotorId);
        District district = districtRepository.getById(postDTO.getDistrictDTO().getDistrictId());
        User user = userRepository.getById(postDTO.getUserDTO().getUserId());

        return new Post(postDTO.getStatusPost(), postDTO.getTitle(),
                postDTO.getModelMotor(), postDTO.getKilometerCount(), postDTO.getDescription(),
                postDTO.getPrice(), postDTO.getSellerName(), postDTO.getSellerPhoneNumber(),
                postDTO.getPostDate(), postDTO.getOwnership(),user,detailMotor,district);
    }

    @Override
    public void remove(Long id) {

    }
}
