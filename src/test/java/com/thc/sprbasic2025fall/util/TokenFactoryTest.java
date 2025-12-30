package com.thc.sprbasic2025fall.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenFactoryTest {

    @Test
    @DisplayName("토큰 암호화 -> 복호화 후 userId 확인")
    void tokenFactoryTest() {
        //given
        Long userId = 100L;

        //when
        String token = TokenFactory.createRefreshToken(userId);

        //then
        Long userIdFromToken = TokenFactory.validateToken(token);
        Assertions.assertThat(userId).isEqualTo(userIdFromToken);
    }

}