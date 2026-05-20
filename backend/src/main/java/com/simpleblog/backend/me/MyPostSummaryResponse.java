package com.simpleblog.backend.me;

import com.simpleblog.backend.post.PostStatus;
import java.time.LocalDateTime;

public record MyPostSummaryResponse(
        Long id,
        String title,
        String summary,
        PostStatus status,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount
) {
}
