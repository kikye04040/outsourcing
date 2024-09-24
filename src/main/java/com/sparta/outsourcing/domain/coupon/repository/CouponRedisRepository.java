package com.sparta.outsourcing.domain.coupon.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.coupon.dto.redis.CouponIssueRedisDto;
import com.sparta.outsourcing.domain.coupon.exception.CouponIssueException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.outsourcing.domain.coupon.util.CouponRedisUtils.getIssueRequestKey;
import static com.sparta.outsourcing.domain.coupon.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@Repository
public class CouponRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<String> issueScript = issueRequestScript();
    private final String issueRequestQueueKey = getIssueRequestQueueKey();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Long sAdd(String key, String value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean sIsMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long rPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void issueRequest(long couponId, String email, int totalIssueQuantity) {
        String issueRequestKey = getIssueRequestKey(couponId);
        CouponIssueRedisDto couponIssueRedisDto = new CouponIssueRedisDto(couponId, email);
        try {
            String code = redisTemplate.execute(
                    issueScript,
                    List.of(issueRequestKey, issueRequestQueueKey),
                    email,
                    String.valueOf(totalIssueQuantity),
                    objectMapper.writeValueAsString(couponIssueRedisDto)
            );
            CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.find(code));
        } catch (JsonProcessingException e) {
            throw new CouponIssueException("쿠폰 발급에 실패했습니다.");
        }
    }

    private RedisScript<String> issueRequestScript() {
        String script = """
                if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                    return '2'
                end
                                
                if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
                    redis.call('SADD', KEYS[1], ARGV[1])
                    redis.call('RPUSH', KEYS[2], ARGV[3])
                    return '1'
                end
                                
                return '3'
                """;
        return RedisScript.of(script, String.class);
    }
}
