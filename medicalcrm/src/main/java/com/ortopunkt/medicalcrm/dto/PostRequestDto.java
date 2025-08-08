package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Post;
import lombok.Data;
import java.time.LocalDate;

@Data

public class PostRequestDto {
    private String title;
    private String platform;
    private LocalDate postDate;
    private Integer reach;
    private Integer likes;
    private Integer comments;
    private Integer shares;
    private Double videoWatchRate;
    private boolean isWeak;

    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setReach(this.reach);
        post.setLikes(this.likes);
        post.setComments(this.comments);
        post.setShares(this.shares);
        post.setVideoWatchRate(this.videoWatchRate);
        post.setWeak(this.isWeak);
        return post;
    }
}
