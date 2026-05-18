package com.simpleblog.backend.comment;

public record CommentAuthorResponse(
        Long id,
        String username,
        String nickname
) {
}
