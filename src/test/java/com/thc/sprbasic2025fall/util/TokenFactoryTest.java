package com.thc.sprbasic2025fall.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenFactoryTest {

    @Autowired TokenFactory tokenFactory;

    @Test
    @DisplayName("토큰 암호화 -> 복호화 후 userId 확인")
    void ㄲtokenFactoryTest() {
        //given
        Long userId = 100L;

        //when
        String token = tokenFactory.createRefreshToken(userId);

        //then
        Long userIdFromToken = tokenFactory.validateToken(token);
        Assertions.assertThat(userId).isEqualTo(userIdFromToken);
    }

}