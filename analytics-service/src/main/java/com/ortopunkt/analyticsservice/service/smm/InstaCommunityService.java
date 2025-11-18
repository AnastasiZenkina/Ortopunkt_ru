package com.ortopunkt.analyticsservice.service.smm;

import com.ortopunkt.analyticsservice.client.VkCommunityClient;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstaCommunityService {

    private final VkCommunityClient vkClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    public PostRequestDto buildInstaReport() {
        log.info("Формирование отчёта по Instagram сообществу...");
        PostRequestDto dto = new PostRequestDto();
        PostResponseDto manual = vkClient.fetchLatestPostFromCrm();

        if (manual != null) {
            dto.setInstaFollowersTotal(manual.getInstaFollowersTotal());
            dto.setMonthYear(manual.getMonthYear());
            dto.setInstaSubscribers(manual.getInstaSubscribers());
            dto.setInstaVisitors(manual.getInstaVisitors());
            dto.setInstaMessages(manual.getInstaMessages());
            dto.setInstaReach(manual.getInstaReach());
            dto.setInstaLikes(manual.getInstaLikes());
            dto.setInstaComments(manual.getInstaComments());
            dto.setInstaShares(manual.getInstaShares());
            dto.setInstaBestPostUrl(manual.getInstaBestPostUrl());
            dto.setInstaWorstPostUrl(manual.getInstaWorstPostUrl());
            log.info("Отчёт Instagram сформирован: " + dto.getMonthYear());
        } else {
            log.warn("Нет данных о постах Instagram за выбранный период");
        }

        return dto;
    }
}