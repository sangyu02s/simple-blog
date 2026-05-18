package com.simpleblog.backend.like;

import com.simpleblog.backend.auth.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/like")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @GetMapping
    public LikeResponse getLikeState(@PathVariable Long postId,
                                     @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return postLikeService.getLikeState(postId, authenticatedUser);
    }

    @PostMapping
    public LikeResponse likePost(@PathVariable Long postId,
                                 @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return postLikeService.likePost(postId, authenticatedUser);
    }

    @DeleteMapping
    public LikeResponse unlikePost(@PathVariable Long postId,
                                   @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return postLikeService.unlikePost(postId, authenticatedUser);
    }
}
