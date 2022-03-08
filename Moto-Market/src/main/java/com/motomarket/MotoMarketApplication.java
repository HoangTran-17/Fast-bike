package com.motomarket;

import com.motomarket.repository.model.Role;
import com.motomarket.repository.model.User;
import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootApplication
public class MotoMarketApplication  {
    @Autowired
    private IUserService userService;

    public static void main(String[] args) {
        SpringApplication.run(MotoMarketApplication.class, args);
    }


}
