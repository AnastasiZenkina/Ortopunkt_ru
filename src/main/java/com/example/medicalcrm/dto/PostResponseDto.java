package com.example.medicalcrm.dto;
import com.example.medicalcrm.entity.Post;
import lombok.Data;
import java.time.LocalDate;

@Data

public class PostResponseDto {
    private Long id;

    private String title;
    private String platform;
    private LocalDate postDate;
    private Integer reach;
    private Integer likes;
    private Integer comments;
    private Integer shares;
    private Double videoWatchRate;
    private boolean isWeak;

    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setPlatform(post.getPlatform());
        dto.setPostDate(post.getPostDate());
        dto.setReach(post.getReach());
        dto.setLikes(post.getLikes());
        dto.setComments(post.getComments());
        dto.setShares(post.getShares());
        dto.setVideoWatchRate(post.getVideoWatchRate());
        dto.setWeak(post.isWeak());
        return dto;
    }
}
