package com.simpleblog.backend.admin;

import com.simpleblog.backend.comment.CommentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentStatusRequest(
        @NotNull(message = "状态不能为空")
        CommentStatus status
) {
}
