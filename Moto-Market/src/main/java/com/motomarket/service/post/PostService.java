package com.motomarket.service.post;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ImageDTO;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Long getCountPost(){
        return postRepository.getCountPost(StatusPost.PUBLIC);
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
    public PostDTO savePost(PostDTO postDTO, UserDTO user, DetailMotorDTO detailMotor,  MultipartFile[] files) {
        Date date = new Date();
        postDTO.setStatusPost(StatusPost.WAITING);
        postDTO.setPostDate(date);
        postDTO.setUserDTO(user);
        postDTO.setDetailMotorDTO(detailMotor);
        Post post = parsePost(postDTO);
        Long postId = saveAndGetPostId(post);  //
        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            ImageDTO image = new ImageDTO();
            image.setImageName(uuid);
            image.setPostId(postId);
            try {
                file.transferTo(new File(rootPath + "/" + uuid + ".png"));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            imageService.save(image);
        }
        Post newPost = postRepository.getById(postId);
        return PostDTO.parsePostDTO(newPost);
    }

    private Long saveAndGetPostId(Post post) {
        Post newPost = postRepository.save(post);
        return newPost.getPostId();
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

    @Override
    public void update(PostDTO postDTO, MultipartFile[] files){
        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();
            ImageDTO image = new ImageDTO();
            image.setImageName(uuid);
            image.setPostId(postDTO.getPostId());
            try {
                file.transferTo(new File(rootPath + "/" + uuid + ".png"));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            imageService.save(image);
        }
        postDTO.setStatusPost(StatusPost.WAITING);

        save(postDTO);

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
    public Page<PostDTO> findTopByFilters(int pageSize,String modelMotor,Integer modelYearMin, Integer modelYearMax,
                                          String province,String typeMotor,Integer capacityMin, Integer capacityMax,
                                          Double priceMin, Double priceMax,String kilometerCount, String colorMotor) {
        Page<Post> posts = postRepository.findTopByFilters(Pageable.ofSize(pageSize),
                modelMotor, modelYearMin,modelYearMax,province,typeMotor, capacityMin, capacityMax,
                priceMin,priceMax,kilometerCount,colorMotor,StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viết đang chờ (WAITING) của 1 user
    @Override
    public Page<PostDTO> findWaitingListByUserId(int pageSize, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(Pageable.ofSize(pageSize), user, StatusPost.WAITING);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viết đang hiển thị (PUBLIC) của 1 user
    @Override
    public Page<PostDTO> findPublicListByUserId(int pageSize, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(Pageable.ofSize(pageSize), user, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viết đang bị ẩn (HIDE) của 1 user
    @Override
    public Page<PostDTO> findHideListByUserId(int pageSize, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(Pageable.ofSize(pageSize), user, StatusPost.HIDE);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viết về xe đã bán (SOLD) của 1 user
    @Override
    public Page<PostDTO> findSoldListByUserId(int pageSize, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(Pageable.ofSize(pageSize), user, StatusPost.SOLD);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List tất cả bài viết của 1 user - trừ StatusPost.DELETE
    @Override
    public Page<PostDTO> findAllByUserId(int pageSize, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUserAndStatusPostNot(Pageable.ofSize(pageSize), user, StatusPost.DELETE);
        return posts.map(PostDTO::parsePostDTO);
    }


    //    Mr Hữu
    @Override
    public PostResponse findPostDeletedIsFalseOrderByDate(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Post> posts = postRepository.findPostDeletedIsFalseOrderByDate(pageable);
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content= listOfPosts.stream().map(PostDTO::parsePostDTO).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse findPostWaitingOrderByDate(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Post> posts = postRepository.findPostWaitingOrderByDate(pageable);
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content= listOfPosts.stream().map(PostDTO::parsePostDTO).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse findPostByTitleLikeOrDetailMotorLikeOrUserNameLikeOrStatusPostLikeOrKilometerCountLikeOrPriceLikeOrProvinceLikeOrDistrictLikeOrPostDateLikeOrOwnershipLike(String key, Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Post> posts = postRepository.findPostByTitleLikeOrDetailMotorLikeOrUserNameLikeOrStatusPostLikeOrKilometerCountLikeOrPriceLikeOrProvinceLikeOrDistrictLikeOrPostDateLikeOrOwnershipLike(key,pageable);
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content= listOfPosts.stream().map(PostDTO::parsePostDTO).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }
}
