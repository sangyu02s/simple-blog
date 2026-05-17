package com.simpleblog.backend.post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long id,
        String title,
        String summary,
        String content,
        PostAuthorResponse author,
        List<String> tags,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount,
        int viewCount
) {
}
