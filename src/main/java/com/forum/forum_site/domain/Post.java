package com.forum.forum_site.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Table(name = "Post")
@Getter
@NoArgsConstructor
@Entity
public class Post{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성을 데이터베이스에 위임
    @Column(name = "post_id")
    private Integer id;

    @Column(length = 40, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String filepath;

    // 익명 게시를 위해 optional = true 추가
    // 지연 로딩 // 관련 entity의 데이터를 다 로드 안해서 성능 최적화 및 트래픽 감소
    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    @Column(nullable = false)
    private Integer likes_count = 9;

    @Column(name = "isHot", nullable = false)
    private Boolean isHot = false; // Hot 게시물 표시

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }


    // ToDo 이게 과연 좋은 방법이 맞는것 일까? 스크랩 페이지 어떻게 보여줄 것인지 고민하기
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    // 유저가 작성한 포스트 리스트에 추가
    public void confirmAuthor(User author) {
        this.author = author;
        author.addPost(this);
    }

    // 유저가 스크랩한 포스트 리스트에 추가
    public void scrapUser(User author) {
        this.author = author;
        author.addScrap(this);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void updateTitle(String title) { this.title = title; }

    public void updateContent(String content) { this.content = content; }
    public void updateFilePath(String filePath) {
        this.filepath = filePath;
    }

    // 좋아요 증가
    public void insertLike() {
        this.likes_count += 1;

        // 좋아요 수가 10개가 되면 isHot을 true로 설정
        if (this.likes_count == 10) {
            this.isHot = true;
        }
    }

    // 좋아요 감소
    public void deleteLike() {
        // 음수가 되지 않도록 체크
        if (this.likes_count > 0) {
            this.likes_count -= 1;
        }

        // 좋아요 10개 미만이면 isHot을 false로 설정
        if (this.likes_count < 10) {
            this.isHot = false;
        }
    }
}
