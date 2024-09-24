package com.sparta.outsourcing.domain.coupon.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.outsourcing.domain.coupon.dto.redis.CouponIssueRedisDto;
import com.sparta.outsourcing.domain.coupon.repository.CouponRedisRepository;
import com.sparta.outsourcing.domain.coupon.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.sparta.outsourcing.domain.coupon.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@EnableScheduling
@Slf4j
@Component
public class CouponIssueListener {

    private final CouponRedisRepository couponRedisRepository;
    private final CouponIssueService couponIssueService;
    private final String issueRequestQueueKey = getIssueRequestQueueKey();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 1000)
    public void issue() throws JsonProcessingException {
        while (existCouponIssueTarget()) {
            CouponIssueRedisDto target = getIssueTarget();
            log.info("발급 시작 target: " + target);
            couponIssueService.issue(target.couponId(), target.email());
            log.info("발급 완료 target: " + target);
            removeIssuedTarget();
        }
    }

    private boolean existCouponIssueTarget() {
        return couponRedisRepository.lSize(issueRequestQueueKey) > 0;
    }

    private CouponIssueRedisDto getIssueTarget() throws JsonProcessingException {
        return objectMapper.readValue(couponRedisRepository.lIndex(issueRequestQueueKey, 0), CouponIssueRedisDto.class);
    }

    private void removeIssuedTarget() {
        couponRedisRepository.lPop(issueRequestQueueKey);
    }
}
