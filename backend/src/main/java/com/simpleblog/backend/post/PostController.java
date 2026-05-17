package com.simpleblog.backend.post;

import com.simpleblog.backend.auth.AuthenticatedUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostDetailResponse createPost(@Valid @RequestBody CreatePostRequest request,
                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return postService.createPost(request, authenticatedUser);
    }

    @GetMapping
    public List<PostSummaryResponse> getPublishedPosts() {
        return postService.getPublishedPosts();
    }

    @GetMapping("/{id}")
    public PostDetailResponse getPublishedPost(@PathVariable Long id) {
        return postService.getPublishedPost(id);
    }
}
