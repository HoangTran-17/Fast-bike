package com.motomarket.service.post;

import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IPostRepository;
import com.motomarket.repository.IUserRepository;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.*;
import com.motomarket.service.filter.CapacityFilter;
import com.motomarket.service.motor.IBrandMotorService;
import com.motomarket.service.motor.ITypeMotorService;
import com.motomarket.service.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private IBrandMotorService brandMotorService;
    @Autowired
    private ITypeMotorService typeMotorService;

    @Override
    public List<PostDTO> findAll() {
        List<PostDTO> postDTOList = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }

    @Override
    public Long getCountPost() {
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
    public Long savePost(PostDTO postDTO, UserDTO user, DetailMotorDTO detailMotor, MultipartFile[] files) {
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
     return postId;
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
        Post post = postRepository.getById(id);
        post.setStatusPost(StatusPost.DELETE);
        postRepository.save(post);
    }

    @Override
    public void setSoldMoto(Long id) {
        Post post = postRepository.getById(id);
        post.setStatusPost(StatusPost.SOLD);
        postRepository.save(post);
    }

    @Override
    public void update(PostDTO postDTO, MultipartFile[] files) {
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
        Post post = postRepository.getById(postDTO.getPostId());
        post.setStatusPost(postDTO.getStatusPost());
        post.setOwnership(postDTO.getOwnership());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setKilometerCount(postDTO.getKilometerCount());
        post.setPrice(postDTO.getPrice());
        post.setSellerName(postDTO.getSellerName());
        post.setSellerPhoneNumber(postDTO.getSellerPhoneNumber());
        post.setProvince(postDTO.getProvince());
        post.setDistrict(postDTO.getDistrict());
        postRepository.save(post);
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
        postRepository.findTopBySeriesMotor(Pageable.ofSize(listSize), seriesMotor, StatusPost.PUBLIC).forEach(post -> {
            postDTOList.add(PostDTO.parsePostDTO(post));
        });
        return postDTOList;
    }

    //  List bài viết mới nhất, tìm kiếm theo modelMotor -"Honda Future 125 2018 Trắng"
    @Override
    public Page<PostDTO> findTopByModelMotorIsLike(Pageable pageable, String modelMotor) {
        Page<Post> posts = postRepository.findTopByModelMotorIsLike(pageable, modelMotor, StatusPost.PUBLIC);
        Page<PostDTO> postDTOS = posts.map(post -> {
            return PostDTO.parsePostDTO(post);
        });
        return postDTOS;
    }


    //  List bài viết mới nhất, tìm kiếm theo province -"Hà Nội"
    @Override
    public Page<PostDTO> findTopByProvince(int pageSize, String province) {
        Page<Post> posts = postRepository.findTopByProvince(Pageable.ofSize(pageSize), province, StatusPost.PUBLIC);
        Page<PostDTO> postDTOS = posts.map(post -> {
            return PostDTO.parsePostDTO(post);
        });
        return postDTOS;
    }


    //  List bài viết mới nhất, tìm kiếm theo typeMotor -"Xe tay ga"
    @Override
    public Page<PostDTO> findTopByTypeMotor(int pageSize, String typeMotor) {
        Page<Post> posts = postRepository.findTopByTypeMotor(Pageable.ofSize(pageSize), typeMotor, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //  List bài viết mới nhất, tìm kiếm theo phân khối -"51 - 174"
    @Override
    public Page<PostDTO> findTopByCapacity(int pageSize, int capacityMin, int capacityMax) {
        Page<Post> posts = postRepository.findTopByCapacity(Pageable.ofSize(pageSize), capacityMin, capacityMax, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //    List bài viêt mới nhất, tìm kiếm theo bộ lọc: modeMotor, province, typeMotor và Capacity.
    @Override
    public Page<PostDTO> findTopByFilters(Pageable pageable, String modelMotor, Integer modelYearMin, Integer modelYearMax,
                                          String province, String typeMotor, Integer capacityMin, Integer capacityMax,
                                          Double priceMin, Double priceMax, String kilometerCount, String colorMotor) {
        Page<Post> posts = postRepository.findTopByFilters(pageable,
                modelMotor, modelYearMin, modelYearMax, province, typeMotor, capacityMin, capacityMax,
                priceMin, priceMax, kilometerCount, colorMotor, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    @Override
    public Page<PostDTO> findTopByFilters1(Pageable pageable,String modelMotor, String brandMotor, String typeMotor, String capacity,
                                           Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax,
                                           String kilometerCount, String color, String province) {

        List<Long> brandIdList = setBrandIdList(brandMotor);
        List<Long> typeIdList = setTypeIdList(typeMotor);
        Integer capacityMin = null;
        Integer capacityMax = null;
        if (capacity != null) {
            Integer[] capa = getCapacityMinMax(capacity);
            if (capa != null) {
                capacityMin = capa[0];
                capacityMax = capa[1];
            }
        }


        Page<Post> posts = postRepository.findTopByFilters1(PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("postDate").descending()),modelMotor, brandIdList,typeIdList,capacityMin,capacityMax, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    private Integer[] getCapacityMinMax(String capacity) {
        List<CapacityFilter> capacityFilterList = getCapacityListSample();
        for (int i = 0; i < capacityFilterList.size(); i++) {
            if (capacityFilterList.get(i).getParam().equals(capacity)) {
                Integer min = capacityFilterList.get(i).getMin();
                Integer max = capacityFilterList.get(i).getMax();
                return new Integer[]{min,max};
            }
        }
        return null;
    }

    private List<Long> setTypeIdList(String typeMotor) {
        List<TypeMotorDTO> typeMotorDTOList = typeMotorService.findAll();
        List<Long> typeIdList = new ArrayList<>();
        if (typeMotor != null) {
            List<String> brList = List.of(typeMotor.split("_"));
            typeMotorDTOList.forEach(typeMotorDTO -> {
                for (int i = 0; i < brList.size(); i++) {
                    if (brList.get(i).equals(typeMotorDTO.getTypeMotorId().toString())) {
                        typeIdList.add(typeMotorDTO.getTypeMotorId());
                        break;
                    }
                }
            });
        }
        return typeIdList;
    }

    private List<Long> setBrandIdList(String brandMotor) {
        List<BrandMotorDTO> brandMotorDTOList = brandMotorService.findAll();
        List<Long> brandIdList = new ArrayList<>();
        if (brandMotor != null) {
            List<String> brList = List.of(brandMotor.split("_"));
            brandMotorDTOList.forEach(brandMotorDTO -> {
                for (int i = 0; i < brList.size(); i++) {
                    if (brList.get(i).equals(brandMotorDTO.getBrandId().toString())) {
                        brandIdList.add(brandMotorDTO.getBrandId());
                        break;
                    }
                }
            });
        }
        return brandIdList;
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
    public Page<PostDTO> findPublicListByUserId(Pageable pageable, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(pageable, user, StatusPost.PUBLIC);
        return posts.map(PostDTO::parsePostDTO);
    }

    //     Số lượng bài viết đang hiển thị (Public) của 1 user
    @Override
    public int getCountPublicPostByUser(User user) {
        return postRepository.countPostByStatusPostAndUser(user, StatusPost.PUBLIC);
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
    public Page<PostDTO> findSoldListByUserId(Pageable pageable, Long userId) {
        User user = userRepository.getById(userId);
        Page<Post> posts = postRepository.findTopByUser(pageable, user, StatusPost.SOLD);
        return posts.map(PostDTO::parsePostDTO);
    }

    //     Số lượng bài viết về xe đã bán (SOLD) của 1 user
    @Override
    public int getCountSoldPostByUser(User user) {
        return postRepository.countPostByStatusPostAndUser(user, StatusPost.SOLD);
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
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findPostDeletedIsFalseOrderByDate(pageable);
        return getPostResponse(posts);
    }

    @Override
    public PostResponse findPostWaitingOrderByDate(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findPostWaitingOrderByDate(pageable);
        return getPostResponse(posts);
    }

    @Override
    public PostResponse findPostHideOrderByDate(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findPostHideOrderByDate(pageable);
        return getPostResponse(posts);
    }

    @Override
    public PostResponse findPostByKeySearch(String key, Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findPostByKeySearch(key, pageable);
        return getPostResponse(posts);
    }


    @Override
    public PostResponse findWaitingPostByKeySearch(String key, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findWaitingPostByKeySearch(key, pageable);
        return getPostResponse(posts);
    }

    @Override
    public PostResponse findHidePostByKeySearch(String key, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findHidePostByKeySearch(key, pageable);
        return getPostResponse(posts);
    }

    @Override
    public void hide(Long id) {
        Post post = postRepository.getById(id);
        post.setStatusPost(StatusPost.HIDE);
        postRepository.save(post);
    }

    @Override
    public void publicPost(Long id) {
        Post post = postRepository.getById(id);
        post.setStatusPost(StatusPost.PUBLIC);
        postRepository.save(post);
    }

    @Override
    public void blockPost(Long id) {
        Post post = postRepository.getById(id);
        post.setStatusPost(StatusPost.BLOCKED);
        postRepository.save(post);
    }

    private PostResponse getPostResponse(Page<Post> posts) {
        List<Post> listOfPosts = posts.getContent();
        List<PostDTO> content = listOfPosts.stream().map(PostDTO::parsePostDTO).collect(Collectors.toList());
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
    public PostResponse findAllByUserIdAndStatusPost(Long userId, StatusPost statusPost, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findAllByUserIdAndStatusPost(userId,statusPost, pageable);
        return getPostResponse(posts);
    }

    @Override
    public String[] setQueryView(String modelMotor, String brandMotor, String typeMotor, String capacity) {
        StringBuilder href = new StringBuilder();
        if (brandMotor!=null) {
            href.append("br=");
            href.append(brandMotor);
            href.append("&");
        }
        if (typeMotor!=null) {
            href.append("tp=");
            href.append(typeMotor);
            href.append("&");
        }
        if (capacity!=null) {
            href.append("cc=");
            href.append(capacity);
            href.append("&");
        }
        String[] query = {modelMotor,String.valueOf(href)};
        return query;
    }

    @Override
    public List<CapacityFilter> getCapacityList(String modelMotor, String brandMotor, String typeMotor, String capacity) {
        List<CapacityFilter> capacityFilterList = getCapacityListSample();
        capacityFilterList.forEach(capacityFilter -> {
            String href = setHref(modelMotor,brandMotor, typeMotor, capacity,capacityFilter.getParam());
            Boolean bo = capacityFilter.getParam().equals(capacity);
            capacityFilter.setHref(href);
            capacityFilter.setSelected(bo);
        });
        return capacityFilterList;
    }

    private String setHref(String modelMotor, String brandMotor, String typeMotor, String capacity, String param) {
        StringBuilder href = new StringBuilder();
        if (modelMotor!=null) {
            href.append("q=");
            href.append(modelMotor);
            href.append("&");
        }
        if (brandMotor!=null) {
            href.append("br=");
            href.append(brandMotor);
            href.append("&");
        }
        if (typeMotor!=null) {
            href.append("tp=");
            href.append(typeMotor);
            href.append("&");
        }

        if (capacity != null) {
            if (!capacity.equals(param)) {
                href.append("cc=");
                href.append(param);
                href.append("&");
            }
        } else{
            href.append("cc=");
            href.append(param);
            href.append("&");
        }
        return String.valueOf(href);
    }

    private List<CapacityFilter> getCapacityListSample() {
        List<CapacityFilter> capacityFilterList = new ArrayList<>();
        capacityFilterList.add(new CapacityFilter(0,50,"0-50","- 50cc"));
        capacityFilterList.add(new CapacityFilter(51,174,"51-174","51-174cc"));
        capacityFilterList.add(new CapacityFilter(175,400,"175-400","175-400cc"));
        capacityFilterList.add(new CapacityFilter(401,750,"401-750cc","401-750cc"));
        capacityFilterList.add(new CapacityFilter(751,1000,"751-1000","751-1000cc"));
        capacityFilterList.add(new CapacityFilter(1000,9999,"1001-9999","1001cc -"));
        return capacityFilterList;
    }
}
