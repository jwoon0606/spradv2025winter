package com.thc.sprbasic2025fall.controller;

import com.thc.sprbasic2025fall.dto.UserDto;
import com.thc.sprbasic2025fall.util.TokenFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RequiredArgsConstructor
@RequestMapping("/api/auth") // 모든 메서드에 앞에 붙는 메핑!!!
@RestController // 페이지 리턴이 아니라, 객체 리턴할꺼에요!
public class AuthRestController {

    final TokenFactory tokenFactory;

    @PostMapping("")
    public ResponseEntity<Void> access(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if(!refreshToken.startsWith("Bearer ")) {   //RefreshToken이 prefix로 시작하지 않으면 돌려보내기
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        refreshToken = refreshToken.substring(7);   //prefix 제거

        String accessToken = tokenFactory.createAccessToken(refreshToken);  //accessToken 발급
        if(accessToken == null) {   //accessToken 정상 발급 확인
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken).build();    //헤더에 prefix 포함된 AccessToken 전달
    }

}
