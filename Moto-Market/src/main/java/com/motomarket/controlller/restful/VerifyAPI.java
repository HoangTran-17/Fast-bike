package com.motomarket.controlller.restful;

import com.motomarket.service.dto.UserDTO;
import com.motomarket.service.user.IUserService;
import com.motomarket.utils.RandomKey;
import com.motomarket.utils.SendKeyToMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
public class VerifyAPI {

    String codeVerify;

    @Autowired
    IUserService userService;

    @GetMapping("/api/verify-mail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) throws MessagingException, UnsupportedEncodingException {
        codeVerify = RandomKey.randomString();
        SendKeyToMail.send(email, "This is your Fast Bike account verification code, enter the code below to complete the verification: " + codeVerify);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/api/verify-code/{code}")
    public ResponseEntity<String> verifyCode(@PathVariable String code) {
        String responseCode;
        if (code.equals(codeVerify)) {
            responseCode = "true";
        } else {
            responseCode = "false";
        }
        return new ResponseEntity<>(responseCode, HttpStatus.OK);
    }

    @GetMapping("/api/existemail/{email}")
    public ResponseEntity<String> checkExistingEmail(@PathVariable String email) {
        String responseCode;
        UserDTO userDTO = userService.findUserByEmail(email);
        if(userDTO==null){
            responseCode = "false";
        } else{
            responseCode = "true";
        }
        return new ResponseEntity<>(responseCode, HttpStatus.OK);
    }

}

