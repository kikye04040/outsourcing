package com.sparta.outsourcing.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RBucket<String> bucket;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void saveRefreshToken_success() {
        // Given
        String token = "refreshToken123";
        String email = "test@example.com";
        long duration = 24L;
        TimeUnit unit = TimeUnit.HOURS;

        when(redissonClient.<String>getBucket("refreshToken:" + email)).thenReturn(bucket);

        // When
        refreshTokenService.saveRefreshToken(token, email, duration, unit);

        // Then
        verify(bucket, times(1)).set(token, duration, unit);
    }

    @Test
    void getRefreshToken_success() {
        // Given
        String email = "test@example.com";
        String expectedToken = "refreshToken123";

        when(redissonClient.<String>getBucket("refreshToken:" + email)).thenReturn(bucket);
        when(bucket.get()).thenReturn(expectedToken);

        // When
        String actualToken = refreshTokenService.getRefreshToken(email);

        // Then
        assertThat(actualToken).isEqualTo(expectedToken);
    }

    @Test
    void deleteRefreshToken_success() {
        // Given
        String email = "test@example.com";

        when(redissonClient.<String>getBucket("refreshToken:" + email)).thenReturn(bucket);

        // When
        refreshTokenService.deleteRefreshToken(email);

        // Then
        verify(bucket, times(1)).delete();
    }

    @Test
    void existsByEmail_success() {
        // Given
        String email = "test@example.com";

        when(redissonClient.<String>getBucket("refreshToken:" + email)).thenReturn(bucket);
        when(bucket.isExists()).thenReturn(true);

        // When
        boolean exists = refreshTokenService.existsByEmail(email);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_notFound() {
        // Given
        String email = "test@example.com";

        when(redissonClient.<String>getBucket("refreshToken:" + email)).thenReturn(bucket);
        when(bucket.isExists()).thenReturn(false);

        // When
        boolean exists = refreshTokenService.existsByEmail(email);

        // Then
        assertThat(exists).isFalse();
    }
}