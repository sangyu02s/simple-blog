package com.simpleblog.backend.post;

import com.simpleblog.backend.auth.AuthenticatedUser;
import com.simpleblog.backend.common.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public PagedResponse<PostSummaryResponse> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "latest") PostSortOption sort
    ) {
        return postService.getPublishedPosts(page, size, sort);
    }

    @GetMapping("/{id}")
    public PostDetailResponse getPublishedPost(@PathVariable Long id) {
        return postService.getPublishedPost(id);
    }
}
