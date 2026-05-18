package com.simpleblog.backend.comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        CommentAuthorResponse author,
        LocalDateTime createdAt
) {
}
