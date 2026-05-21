package com.simpleblog.backend.tag;

import com.simpleblog.backend.common.PagedResponse;
import com.simpleblog.backend.post.PostService;
import com.simpleblog.backend.post.PostSortOption;
import com.simpleblog.backend.post.PostSummaryResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final PostService postService;

    public TagController(TagService tagService, PostService postService) {
        this.tagService = tagService;
        this.postService = postService;
    }

    @GetMapping
    public List<TagResponse> getTags() {
        return tagService.getTags();
    }

    @GetMapping("/{slug}/posts")
    public PagedResponse<PostSummaryResponse> getPostsByTag(
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "latest") PostSortOption sort
    ) {
        return postService.getPostsByTag(slug, page, size, sort);
    }
}
