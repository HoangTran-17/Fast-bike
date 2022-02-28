package com.motomarket.utils;//package com.codegym.utils;
//
//import com.codegym.service.jwt.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class AppUtils {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtService jwtService;
//
//
//    @Autowired
//    private IUserService userService;
//
//    public static String removeNonAlphanumeric(String str) {
//        do {
//            str = str.replace(" ", "-");
//            str = str.replaceAll("[^a-zA-Z0-9\\-]", "-");
//            str = str.replaceAll("--", "-");
//
//            while (str.charAt(0) == '-') {
//                str = str.substring(1);
//            }
//
//            while (str.charAt(str.length() - 1) == '-') {
//                str = str.substring(0, str.length() - 1);
//            }
//        }
//        while (str.contains("--"));
//
//        return str.trim().toLowerCase();
//    }
//
//    public ResponseEntity<?> mapErrorToResponse(BindingResult result) {
//        List<FieldError> fieldErrors = result.getFieldErrors();
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError fieldError : fieldErrors) {
//            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//
//    public static Date convertoString(String stringDate) {
//        Date date = new Date();
//        try {
//            date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//}
