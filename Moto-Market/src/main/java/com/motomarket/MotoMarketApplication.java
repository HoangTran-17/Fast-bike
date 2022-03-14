package com.motomarket;

import com.motomarket.repository.SampleRepository;
import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.Sample;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.BrandFilter;
import com.motomarket.service.dto.PostDTO;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.motor.IBrandMotorService;
import com.motomarket.service.post.IPostService;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MotoMarketApplication implements CommandLineRunner {
    @Autowired
    private IUserService userService;
    @Autowired
    private IBrandMotorService brandMotorService;
    @Autowired
    private IPostService postService;
    @Autowired
    private SampleRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(MotoMarketApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
//        String br = "1_2_45";
////        String tp = "3";
////        String cc = "a";
////        List<BrandFilter> brandList = brandMotorService.getAllBrandFilter(br, tp, cc);
////        System.out.println(brandList);
//        System.out.println(br);
//        Page<PostDTO> postDTOS = postService.findTopByFilters1(25, br, null, null, null,
//                null, null, null, null, null, null, null);
//        System.out.println(postDTOS);
////        List<String> list = new java.util.ArrayList<>(List.of(br.split("_")));
////        System.out.println(list);
////        String add = "2";
////        int i = list.indexOf(add);
////        System.out.println(i);
////        if (i == -1) {
////            list.add(add);
////        } else {
////            list.remove(i);
////        }
////        br = String.join("_", list);
////        System.out.println(br);
////        System.out.println(list);
//        List<Integer> ids=new ArrayList<>();
//        ids.add(1);
//        ids.add(2);
//        List<Sample> reuls = repository.findIn(new ArrayList<>());
//        System.out.println(reuls);
    }
}
