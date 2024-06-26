package com.imooc.mall.util;

import com.imooc.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
  public static String getMD5Str(String passWord) throws NoSuchAlgorithmException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    return Base64.encodeBase64String(md5.digest((passWord+ Constant.SALT).getBytes()));
  }
}