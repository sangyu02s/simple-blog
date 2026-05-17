package com.simpleblog.backend.post;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummaryResponse(
        Long id,
        String title,
        String summary,
        PostAuthorResponse author,
        List<String> tags,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount
) {
}
