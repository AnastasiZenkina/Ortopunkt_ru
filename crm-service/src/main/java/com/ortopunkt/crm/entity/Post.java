package com.ortopunkt.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vkMembersTotal;
    private Long instaFollowersTotal;
    private String bestPostUrl;
    private String bestPostTitle;
    private String worstPostUrl;
    private String worstPostTitle;
    private String instaBestPostUrl;
    private String instaWorstPostUrl;

    private String title;
    private LocalDate postDate;
    private Integer views;

    private Integer vkSubscribers;
    private Integer vkVisitors;
    private Integer vkMessages;
    private Integer vkReach;
    private Integer vkLikes;
    private Integer vkComments;
    private Integer vkShares;

    private Integer instaSubscribers;
    private Integer instaVisitors;
    private Integer instaMessages;
    private Integer instaReach;
    private Integer instaLikes;
    private Integer instaComments;
    private Integer instaShares;

    private String monthYear;
}