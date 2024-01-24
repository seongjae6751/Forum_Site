package com.forum.forum_site.controller;

import com.forum.forum_site.dto.SaveCommentDto;
import com.forum.forum_site.dto.UpdateCommentDto;
import com.forum.forum_site.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void incresaseLike(@PathVariable("postId") Integer postId) {
        likeService.insert(postId);
    }

    @DeleteMapping("/{postId}")
    public void deleteLike(@PathVariable("postId") Integer postId) {
        likeService.delete(postId);
    }
}
