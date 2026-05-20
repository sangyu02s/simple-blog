package com.simpleblog.backend.admin;

import com.simpleblog.backend.post.PostStatus;
import java.time.LocalDateTime;

public record AdminPostSummaryResponse(
        Long id,
        String title,
        String authorNickname,
        PostStatus status,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount
) {
}
