package com.thc.sprbasic2025fall.util;

import com.thc.sprbasic2025fall.domain.RefreshToken;
import com.thc.sprbasic2025fall.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TokenFactory {

    final RefreshTokenRepository refreshTokenRepository;

    static int refreshTokenValidityHour = 12;
    static int accessTokenValidityHour = 1;

    public String createToken(Long userId, int termHour) {
        LocalDateTime due = LocalDateTime.now().plusHours(termHour);
        String token = null;
        try {
            token = AES256Cipher.AES_Encode(null,userId+"_"+due);
        } catch (Exception e){}

        return token;
    }

    // 일단, 리프레시 토큰 만들기
    public String createRefreshToken(Long userId) {
        return createToken(userId, refreshTokenValidityHour);
    }

    // 엑세스 토큰 만들기
    public String createAccessToken(String refreshToken) {
        Long userId = validateToken(refreshToken);

        RefreshToken entity = refreshTokenRepository.findByContent(refreshToken);
        if(entity == null){
            return null;
        }
        Long userIdFromToken = entity.getUserId();
        System.out.println("userIdFromToken : " + userIdFromToken);
        if(!userIdFromToken.equals(userId)){
            return null;
        }

        System.out.println("userId : " + userId);
        if(userId == null){
            return null;
        }
        return createToken(userId, accessTokenValidityHour);
    }

    public Long validateToken(String token) {
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