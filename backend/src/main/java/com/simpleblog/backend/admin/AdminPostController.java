package com.simpleblog.backend.admin;

import com.simpleblog.backend.post.PostStatus;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/posts")
public class AdminPostController {

    private final AdminPostService adminPostService;

    public AdminPostController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @GetMapping
    public List<AdminPostSummaryResponse> getPosts(
            @RequestParam(required = false) PostStatus status,
            @RequestParam(defaultValue = "") String keyword
    ) {
        return adminPostService.getPosts(status, keyword);
    }

    @PatchMapping("/{postId}/status")
    public AdminPostSummaryResponse updateStatus(@PathVariable Long postId,
                                                 @Valid @RequestBody UpdatePostStatusRequest request) {
        return adminPostService.updateStatus(postId, request);
    }
}
