package com.simpleblog.backend.like;

public record LikeResponse(
        boolean liked,
        int likeCount
) {
}
