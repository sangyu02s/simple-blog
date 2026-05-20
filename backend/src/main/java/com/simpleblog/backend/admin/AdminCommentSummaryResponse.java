package com.simpleblog.backend.admin;

import com.simpleblog.backend.comment.CommentStatus;
import java.time.LocalDateTime;

public record AdminCommentSummaryResponse(
        Long id,
        String postTitle,
        String authorNickname,
        String content,
        CommentStatus status,
        LocalDateTime createdAt
) {
}
