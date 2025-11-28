package com.ortopunkt.crm.service.post;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.crm.entity.Post;

public class PostMapper {

    public static void applyDtoToEntity(PostRequestDto dto, Post post) {
        if (dto.getMonthYear() != null) post.setMonthYear(dto.getMonthYear());
        if (dto.getVkMembersTotal() != null) post.setVkMembersTotal(dto.getVkMembersTotal());
        if (dto.getInstaFollowersTotal() != null) post.setInstaFollowersTotal(dto.getInstaFollowersTotal());
        if (dto.getVkSubscribers() != null) post.setVkSubscribers(dto.getVkSubscribers());
        if (dto.getVkVisitors() != null) post.setVkVisitors(dto.getVkVisitors());
        if (dto.getVkMessages() != null) post.setVkMessages(dto.getVkMessages());
        if (dto.getVkReach() != null) post.setVkReach(dto.getVkReach());
        if (dto.getVkLikes() != null) post.setVkLikes(dto.getVkLikes());
        if (dto.getVkComments() != null) post.setVkComments(dto.getVkComments());
        if (dto.getVkShares() != null) post.setVkShares(dto.getVkShares());
        if (dto.getBestPostUrl() != null) post.setBestPostUrl(dto.getBestPostUrl());
        if (dto.getBestPostTitle() != null) post.setBestPostTitle(dto.getBestPostTitle());
        if (dto.getWorstPostUrl() != null) post.setWorstPostUrl(dto.getWorstPostUrl());
        if (dto.getWorstPostTitle() != null) post.setWorstPostTitle(dto.getWorstPostTitle());
        if (dto.getInstaSubscribers() != null) post.setInstaSubscribers(dto.getInstaSubscribers());
        if (dto.getInstaVisitors() != null) post.setInstaVisitors(dto.getInstaVisitors());
        if (dto.getInstaMessages() != null) post.setInstaMessages(dto.getInstaMessages());
        if (dto.getInstaReach() != null) post.setInstaReach(dto.getInstaReach());
        if (dto.getInstaLikes() != null) post.setInstaLikes(dto.getInstaLikes());
        if (dto.getInstaComments() != null) post.setInstaComments(dto.getInstaComments());
        if (dto.getInstaShares() != null) post.setInstaShares(dto.getInstaShares());
        if (dto.getInstaBestPostUrl() != null) post.setInstaBestPostUrl(dto.getInstaBestPostUrl());
        if (dto.getInstaWorstPostUrl() != null) post.setInstaWorstPostUrl(dto.getInstaWorstPostUrl());
        if (dto.getPostId() != null) post.setPostId(dto.getPostId());
        if (dto.getTitle() != null) post.setTitle(dto.getTitle());
        if (dto.getPostDate() != null) post.setPostDate(dto.getPostDate());
        if (dto.getViews() != null) post.setViews(dto.getViews());
    }

    public static void applyVkManual(PostRequestDto dto, Post post) {
        applyDtoToEntity(dto, post);
    }

    public static void applyInstaManual(PostRequestDto dto, Post post) {
        applyDtoToEntity(dto, post);
    }

    public static PostResponseDto toResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setMonthYear(post.getMonthYear());
        dto.setVkMembersTotal(post.getVkMembersTotal());
        dto.setInstaFollowersTotal(post.getInstaFollowersTotal());
        dto.setVkSubscribers(post.getVkSubscribers());
        dto.setVkVisitors(post.getVkVisitors());
        dto.setVkMessages(post.getVkMessages());
        dto.setVkReach(post.getVkReach());
        dto.setVkLikes(post.getVkLikes());
        dto.setVkComments(post.getVkComments());
        dto.setVkShares(post.getVkShares());
        dto.setBestPostUrl(post.getBestPostUrl());
        dto.setBestPostTitle(post.getBestPostTitle());
        dto.setWorstPostUrl(post.getWorstPostUrl());
        dto.setWorstPostTitle(post.getWorstPostTitle());
        dto.setInstaSubscribers(post.getInstaSubscribers());
        dto.setInstaVisitors(post.getInstaVisitors());
        dto.setInstaMessages(post.getInstaMessages());
        dto.setInstaReach(post.getInstaReach());
        dto.setInstaLikes(post.getInstaLikes());
        dto.setInstaComments(post.getInstaComments());
        dto.setInstaShares(post.getInstaShares());
        dto.setInstaBestPostUrl(post.getInstaBestPostUrl());
        dto.setInstaWorstPostUrl(post.getInstaWorstPostUrl());
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setPostDate(post.getPostDate());
        dto.setViews(post.getViews());
        return dto;
    }
}