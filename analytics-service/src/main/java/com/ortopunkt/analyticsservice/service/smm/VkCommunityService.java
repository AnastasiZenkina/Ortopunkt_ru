package com.ortopunkt.analyticsservice.service.smm;

import com.fasterxml.jackson.databind.JsonNode;
import com.ortopunkt.analyticsservice.client.VkCommunityClient;
import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VkCommunityService {

    private final VkCommunityClient vkClient;
    private final ServiceLogger log = new ServiceLogger(getClass(), "ANALYTICS");

    @Value("${vk.group.id}")
    private String groupId;

    public PostRequestDto buildVkReport() {
        log.info("Формирование отчёта по сообществу VK...");
        PostRequestDto dto = new PostRequestDto();
        dto.setVkMembersTotal(vkClient.fetchMembersCount());

        YearMonth prev = YearMonth.now().minusMonths(1);
        LocalDate from = prev.atDay(1);
        LocalDate to = prev.atEndOfMonth();
        String monthYear = prev.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru")) + " " + prev.getYear();
        dto.setMonthYear(monthYear);

        JsonNode items = vkClient.fetchPosts(0, 100);
        List<PostRequestDto> posts = new ArrayList<>();

        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                LocalDate date = Instant.ofEpochSecond(item.path("date").asLong())
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                if (date.isBefore(from) || date.isAfter(to)) continue;

                PostRequestDto post = new PostRequestDto();
                post.setPostDate(date);
                post.setViews(item.path("views").path("count").asInt(0));
                post.setVkLikes(item.path("likes").path("count").asInt(0));
                post.setVkComments(item.path("comments").path("count").asInt(0));
                post.setVkShares(item.path("reposts").path("count").asInt(0));
                post.setTitle(item.path("text").asText(""));
                post.setPostId(item.path("id").asLong());
                posts.add(post);
            }
        }

        if (!posts.isEmpty()) {
            PostRequestDto best = findBest(posts);
            PostRequestDto worst = findWorst(posts);
            if (best != null) {
                dto.setBestPostUrl("https://vk.com/wall-" + groupId + "_" + best.getPostId());
                dto.setBestPostTitle(shortTitle(best));
                log.info("Лучший пост: " + dto.getBestPostUrl());
            }
            if (worst != null) {
                dto.setWorstPostUrl("https://vk.com/wall-" + groupId + "_" + worst.getPostId());
                dto.setWorstPostTitle(shortTitle(worst));
                log.info("Худший пост: " + dto.getWorstPostUrl());
            }
        } else {
            log.warn("Постов за период не найдено");
        }

        PostResponseDto manual = vkClient.fetchLatestPostFromCrm();
        if (manual != null) {
            dto.setVkSubscribers(manual.getVkSubscribers());
            dto.setVkVisitors(manual.getVkVisitors());
            dto.setVkMessages(manual.getVkMessages());
            dto.setVkReach(manual.getVkReach());
            dto.setVkLikes(manual.getVkLikes());
            dto.setVkComments(manual.getVkComments());
            dto.setVkShares(manual.getVkShares());
            log.info("Добавлены метрики из CRM");
        } else {
            log.warn("Нет данных CRM для отчёта VK");
        }

        return dto;
    }

    private PostRequestDto findBest(List<PostRequestDto> posts) {
        return posts.stream().max(Comparator.comparingInt(this::score)).orElse(null);
    }

    private PostRequestDto findWorst(List<PostRequestDto> posts) {
        return posts.stream().min(Comparator.comparingInt(this::score)).orElse(null);
    }

    private int score(PostRequestDto p) {
        if (p.getViews() != null && p.getViews() > 0) return p.getViews();
        return (p.getVkLikes() != null ? p.getVkLikes() : 0)
                + (p.getVkComments() != null ? p.getVkComments() * 2 : 0)
                + (p.getVkShares() != null ? p.getVkShares() * 3 : 0);
    }

    private String shortTitle(PostRequestDto p) {
        String text = Optional.ofNullable(p.getTitle()).orElse("");
        return text.isEmpty() ? "Пост от " + p.getPostDate()
                : (text.length() > 100 ? text.substring(0, 97) + "..." : text);
    }
}