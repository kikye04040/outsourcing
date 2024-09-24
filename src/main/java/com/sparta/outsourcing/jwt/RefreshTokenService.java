package com.sparta.outsourcing.jwt;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RedissonClient redissonClient;

    public void saveRefreshToken(String token, String email, long duration, TimeUnit unit) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + email);
        bucket.set(token, duration, unit);  // Redis에 토큰을 저장하고 만료 시간을 설정
    }

    public String getRefreshToken(String email) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + email);
        return bucket.get();
    }

    public void deleteRefreshToken(String email) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + email);
        bucket.delete();
    }

    public boolean existsByEmail(String email) {
        RBucket<Object> bucket = redissonClient.getBucket("refreshToken:" + email);
        return bucket.isExists();
    }
}
