package com.imooc.mall.util;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//Email工具
public class EmailUtil {

    //验证代码
    public static String genVerificationCode() {
         List<String> veriList = Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "n", "n", "o", "p",
                "q", "p", "s", "t", "u", "v",
                "w", "x", "y", "z"});
        Collections.shuffle(veriList);
        String result="";
        for (int i = 0; i < 6; i++) {
            result+=veriList.get(i);
        }
        return result;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        InternetAddress internetAddress = null;
        try {
            internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
