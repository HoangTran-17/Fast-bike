package com.motomarket.utils;

import org.apache.commons.lang3.RandomStringUtils;


public class RandomKey {
   public static String randomString(){
       return RandomStringUtils.random(6, true, true);
   }
}
