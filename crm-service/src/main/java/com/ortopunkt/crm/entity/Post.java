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

    private String title;
    private String platform;
    private LocalDate postDate;
    private Integer reach;
    private Integer likes;
    private Integer comments;
    private Integer shares;
    private Double videoWatchRate;
    private boolean isWeak;



}
