package com.simpleblog.backend.comment;

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
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getVisibleComments(@PathVariable Long postId) {
        return commentService.getVisibleComments(postId);
    }

    @PostMapping
    public CommentResponse createComment(@PathVariable Long postId,
                                         @Valid @RequestBody CreateCommentRequest request,
                                         @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return commentService.createComment(postId, request, authenticatedUser);
    }
}
