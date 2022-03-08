package com.motomarket.service.post;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService implements IPostService {
    @Value("${server.rootPath}")
    private String rootPath;

    @Autowired
    private IPostRepository postRepository;
    @Autowired
    private IDetailMotorRepository detailMotorRepository;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IImageService imageService;

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
        PostDTO newPostDTO = PostDTO.parsePostDTO(postRepository.save(post));
        return newPostDTO;
    }

    @Override
    public PostDTO savePost(PostDTO post, UserDTO user, DetailMotorDTO detailMotor, Long ownershipSelect, MultipartFile[] files) {
        Date date = new Date();
        post.setStatusPost(StatusPost.PUBLIC);
        post.setPostDate(date);
        if (ownershipSelect == 0) {
            post.setOwnership(Ownership.OWNERSHIP);
        } else {
            post.setOwnership(Ownership.NO_OWNERSHIP);
        }
        post.setUserDTO(user);
        post.setDetailMotorDTO(detailMotor);
        PostDTO newPost = save(post);
        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            ImageDTO image = new ImageDTO();
            image.setImageName(uuid);
            image.setPostId(newPost.getPostId());
            try {
                file.transferTo(new File(rootPath + "/" + uuid + ".png"));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            imageService.save(image);
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
                postDTO.getPostDate(), postDTO.getOwnership(), user, detailMotor);
    }

    @Override
    public void remove(Long id) {

    }
//    List bài viết mới nhất
    @Override
    public List<PostDTO> findListOfLatestPosts(int listSize) {
        List<PostDTO> postDTOList = new ArrayList<>();
        postRepository.findTopByStatusPost(StatusPost.PUBLIC, Pageable.ofSize(listSize)).forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }

    //  List bài viết mới nhất, tìm kiếm theo seriesMotor -"Honda Future 125"
    @Override
    public List<PostDTO> findTopBySeriesMotor(int listSize, String seriesMotor) {
        List<PostDTO> postDTOList = new ArrayList<>();
        postRepository.findTopBySeriesMotor(Pageable.ofSize(listSize),seriesMotor,StatusPost.PUBLIC).forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }

    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    @Override
    public Page<PostDTO> findTopByModelMotorIsLike(int pageSize,String modelMotor) {
        Page<Post> posts = postRepository.findTopByModelMotorIsLike(Pageable.ofSize(pageSize), modelMotor, StatusPost.PUBLIC);
        Page<PostDTO> postDTOS = posts.map(post -> {
            return PostDTO.parsePostDTO(post);
        });
        return postDTOS;
    }


    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    @Override
    public Page<PostDTO> findTopByProvince(int pageSize,String province) {
        Page<Post> posts = postRepository.findTopByProvince(Pageable.ofSize(pageSize),province,StatusPost.PUBLIC);
        Page<PostDTO> postDTOS = posts.map(post -> {
            return PostDTO.parsePostDTO(post);
        });
        return postDTOS;
    }


    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    @Override
    public Page<PostDTO> findTopByTypeMotor(int pageSize,String typeMotor) {
        Page<Post> posts = postRepository.findTopByTypeMotor(Pageable.ofSize(pageSize),typeMotor,StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    @Override
    public Page<PostDTO> findTopByCapacity(int pageSize,int capacityMin, int capacityMax) {
        Page<Post> posts = postRepository.findTopByCapacity(Pageable.ofSize(pageSize), capacityMin, capacityMax, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    @Override
    public Page<PostDTO> findTopByFilters(int pageSize,String modelMotor,int modelYearMin, int modelYearMax,
                                          String province,String typeMotor,int capacityMin, int capacityMax,
                                          Double priceMin, Double priceMax,String kilometerCount, String colorMotor) {
        Page<Post> posts = postRepository.findTopByFilters(Pageable.ofSize(pageSize),
                modelMotor, modelYearMin,modelYearMax,province,typeMotor, capacityMin, capacityMax,
                priceMin,priceMax,kilometerCount,colorMotor,StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }


}
//Pageable pageable,@Param("modelMotor") String modelMotor,
//                    @Param("modelYearMin") Integer modelYearMin, @Param("modelYearMax") Integer modelYearMax,
//                    @Param("province") String province,
//                    @Param("typeMotor") String typeMotor,
//                    @Param("capacityMin") Integer capacityMin, @Param("capacityMax") Integer capacityMax,
//                    @Param("priceMin") Double priceMin, @Param("priceMax") Double priceMax,
//                    @Param("km") String kilometerCount,
//                    @Param("color") String colorMotor,
//                    @Param("statusPost") StatusPost statusPost);