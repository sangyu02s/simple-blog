package com.simpleblog.backend.admin;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comments")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    public AdminCommentController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @GetMapping
    public List<AdminCommentSummaryResponse> getComments() {
        return adminCommentService.getComments();
    }

    @PatchMapping("/{commentId}/status")
    public AdminCommentSummaryResponse updateStatus(@PathVariable Long commentId,
                                                    @Valid @RequestBody UpdateCommentStatusRequest request) {
        return adminCommentService.updateStatus(commentId, request);
    }
}
