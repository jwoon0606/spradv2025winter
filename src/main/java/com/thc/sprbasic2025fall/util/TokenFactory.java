package com.thc.sprbasic2025fall.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class TokenFactory {

    static int refreshTokenValidityHour = 2;

    // 일단, 리프레시 토큰 만들기
    public static String createRefreshToken(Long userId) {
        LocalDateTime due = LocalDateTime.now().plusHours(refreshTokenValidityHour);
        String token = null;
        try {
            token = AES256Cipher.AES_Encode(null,userId+"_"+due);
        } catch (Exception e){}

        return token;
    }

    public static Long validateToken(String token) {
        String info = null;
        try{
            info = AES256Cipher.AES_Decode(null, token);

            String[] array_info = info.split("_");
            Long userId = Long.parseLong(array_info[0]);
            String due = array_info[1];

            String now = LocalDateTime.now().toString();
            String[] tempArray = {due, now};
            Arrays.sort(tempArray);
            // asc 로 정렬하네!
            // 앞에 있는 것이 현재 시간이어야만 하네!
            // tempArray[0] === nowTime?!
            if(now.equals(tempArray[0])){
                return userId;
            }
        } catch (Exception e){}

        return null;
    }

}